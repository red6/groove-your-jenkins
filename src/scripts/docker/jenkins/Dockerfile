FROM java:8
RUN apt-get update -qq && apt-get install -qqy curl apt-transport-https iptables ca-certificates lxc git-core zip graphviz
RUN curl -sSL https://get.docker.com/ | sh

RUN curl -sf -o /opt/apache-maven-bin.tar.gz http://apache.mirror.digionline.de/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz; tar xzf /opt/apache-maven-bin.tar.gz -C /opt/; rm /opt/apache-maven-bin.tar.gz; ln -s /opt/apache-maven-3.3.3 /opt/maven

ENV JENKINS_HOME /opt/jenkins
ENV JENKINS_MIRROR http://mirrors.jenkins-ci.org
RUN mkdir -p $JENKINS_HOME
RUN curl -sf -o /opt/jenkins/jenkins.war -L $JENKINS_MIRROR/war/latest/jenkins.war
RUN mkdir -p $JENKINS_HOME/plugins; for plugin in greenballs scm-api git-client git swarm workflow-aggregator workflow-job workflow-api workflow-cps workflow-durable-task-step workflow-basic-steps workflow-cps-global-lib workflow-scm-step workflow-support workflow-step-api durable-task ace-editor jquery-detached git-server job-dsl; do curl -sf -o $JENKINS_HOME/plugins/${plugin}.hpi -L $JENKINS_MIRROR/plugins/${plugin}/latest/${plugin}.hpi; done

VOLUME $JENKINS_HOME/data
WORKDIR $JENKINS_HOME
EXPOSE 8080
CMD [ "java", "-jar", "jenkins.war" ]
