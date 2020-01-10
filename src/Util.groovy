class Util implements Serializable {

  @NonCPS
  static def envForBranch(String branch) {
    if (branch == null || branch == '' ) {
      return 'emtpy'
    }

  	 if (branch == 'develop') {
  		  return 'dev'
  	 } else if (branch.startsWith('release/')) {
  		  return 'sit'
  	 } else if (branch.startsWith('master')) {
  		  return 'prd'
  	 } else {
        return 'non'
     }
  }

}
