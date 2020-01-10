def shortGitHash() {
	gitHash = build.getBuildVariables().get('GIT_COMMIT')
	return gitHash[-8..-1]
}

def envForBranch(string branchName) {
	branch = build.getBuildVariables().get('GIT_BRANCH')
  	if (branch == 'develop') {
  		return 'dev'
  	} elif (branch.startsWith('release/')) {
  		return 'sit'
  	} else {
  		return 'any'
  	}
}
