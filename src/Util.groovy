class Util {

  def envForBranch(String branchName) {
	   branch = build.getBuildVariables().get('GIT_BRANCH')
  	 if (branch == 'develop') {
  		  return 'dev'
  	 } else if (branch.startsWith('release/')) {
  		  return 'sit'
  	 } else {
  		  return 'any'
  	 }
  }

}
