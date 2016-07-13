job('rebetol-build') {
  scm {
    git('git://capitol.de/rebetol.git')
  }
  triggers {
    scm('*/15 * * * *')
  }
  steps {
    maven('clean verify')
  }
}
