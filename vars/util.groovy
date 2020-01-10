static String shortGitHash() {
	return env.GIT_COMMIT[-8..-1]
}

static String envForBranch(String branchName) {
  	if (env.GIT_BRANCH == 'develop') {
  		return 'dev'
  	} else if (branchName.startsWith('release/')) {
  		return 'sit'
  	} else {
  		return 'any'
  	}
}
