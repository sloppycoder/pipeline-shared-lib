def shortGitHash() {
	return env.GIT_COMMIT[-8..-1]
}

def envForBranch(String branchName) {
  	if (env.GIT_BRANCH == 'develop') {
  		return 'dev'
  	} else if (branchName.startsWith('release/')) {
  		return 'sit'
  	} else {
  		return 'any'
  	}
}
