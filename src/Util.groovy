class Util implements Serializable {

  static def envForBranch(String branch) {
    System.out.println(String.format("*** %s ***"), branch)
  	 if (branch == 'develop') {
  		  return 'dev'
  	 } else if (branch.startsWith('release/')) {
  		  return 'sit'
  	 } else {
  		  return 'any'
  	 }
  }

}
