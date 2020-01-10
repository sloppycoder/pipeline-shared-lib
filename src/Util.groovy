class Util {

  def shortGitHash() {
	   gitHash = build.getBuildVariables().get('GIT_COMMIT')
	   return gitHash[-8..-1]
  }

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