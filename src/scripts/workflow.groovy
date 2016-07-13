def majorVersionNumber = 1.0
def version = "${majorVersionNumber}.${env.BUILD_NUMBER}"

def scriptsHome = "src/scripts"
def webAppHome = "continuous-delivery-example-webapp"
def testHome = "continuous-delivery-example-test"
def releaseBranch = "cd-release-${version}"
def nexusBaseRepoUrl = "http://nexus:8081/content/repositories/releases"
def webAppDownloadURL = "${nexusBaseRepoUrl}/de/ppi/sf1/experimental/continuous-delivery-example-webapp/${version}/continuous-delivery-example-webapp-${version}.war"
def dockerScriptsHome="src/scripts/docker"

def deployOnDockerTomcat = {name, port -> 
  sh "wget -O ${dockerScriptsHome}/tomcat/continuous-delivery-example-webapp.war ${webAppDownloadURL}"
  sh "docker build -t grohmann/tomcat:${version} ${dockerScriptsHome}/tomcat/"
  sh "docker stop ${name} || echo \"Container was not running\""
  sh "docker run --rm --name ${name} -p ${port}:8080 grohmann/tomcat:${version} &"
}

def runTestsOnDockerTomcat = {mvnHome, name -> 
  sh "${scriptsHome}/createEnvProperties.sh ${name}"
  sh "${mvnHome}/bin/mvn -B -f ${testHome} test"
}

def deployToArtifactRepository = { mvnHome ->
	sh "tar cfvz ${webAppHome}/target/site.tar.gz ${webAppHome}/target/site/"
    sh """${mvnHome}/bin/mvn deploy:deploy-file -DpomFile=${webAppHome}/pom.xml \\
    				-Dfile=${webAppHome}/target/continuous-delivery-example-webapp.war \\
    				-Durl=${nexusBaseRepoUrl} \\
    				-DrepositoryId=LocalNexus \\
    				-Dfiles=${webAppHome}/target/site.tar.gz  \\
    				-Dtypes=tar.gz \\
    				-Dclassifiers=site""" 

}

def commitAndPushReleaseBranch = {
    sh "git commit -am \"New release candidate\""
    sh "git push origin ${releaseBranch}"
}

def deleteReleaseBranch = {
  sh "git checkout HEAD^" 
  sh "git checkout -f origin/master"
  sh "git branch -D ${releaseBranch}"
}

node('master') {

  def mvnHome = tool 'M3'

  stage 'Commit Stage'

  sh "git config --global user.email buildadm@ppi.de; git config --global user.name buildadm"
  
  git url: 'git@igit.ppi.int:rgr/continuous-delivery-example.git'

  // Create release branch
  sh "git checkout -b ${releaseBranch}"
  
  // Set build version
  sh "${mvnHome}/bin/mvn -B versions:set -DnewVersion=${version}"
  try {
    // Execute maven build
    sh "${mvnHome}/bin/mvn -B -f ${webAppHome} verify"
    
	commitAndPushReleaseBranch()
        
    deployToArtifactRepository(mvnHome)   			
    
  } catch(e) {
      // Delete release branch if the maven build fails
      deleteReleaseBranch()
      error "BUILD FAILED"
  }
  
 }
  
stage  name: 'Acceptance Test Stage', concurrency: 3
  
node('master') {

  def mvnHome = tool 'M3'

  deployOnDockerTomcat("tomcat-cd-auto-${version}", '')
  
  try {
  	runTestsOnDockerTomcat(mvnHome, "tomcat-cd-auto-${version}")
  } finally {
    sh "docker stop tomcat-cd-auto-${version}"  
  }
}
  
stage  name: 'User Acceptance Test Stage', concurrency: 1
  
input "Deploy to User Acceptance Test-Stage?"

node('master') {  

  deployOnDockerTomcat('tomcat-cd-user', 8083)
  
  echo "http://localhost:8083/continuous-delivery-example-webapp"
  
}

