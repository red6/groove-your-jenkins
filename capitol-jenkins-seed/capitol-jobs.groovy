def projects = ['rebetol', 'lebetol']
projects.each { project ->

  maven("${project}-build") {
    scm {
      git("git://capitol.de/${project}.git")
    }
    goals('clean verify')
  }

  maven("${project}-acceptance-test") {
    scm {et
      git("git://capitol.de/${project}.git")
    }
    goals('integration-test')
  }

}
