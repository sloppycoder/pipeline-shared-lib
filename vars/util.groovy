def shortGitHash() {
	return env.GIT_COMMIT[-8..-1]
}

def envForBranch(string branchName) {
  	if (env.GIT_BRANCH == 'develop') {
  		return 'dev'
  	} elif (branchName.startsWith('release/')) {
  		return 'sit'
  	} else {
  		return 'any'
  	}
}
