class Util implements Serializable {

    static String envForBranch(String branch) {
        def envMap = [
            'dev'     : 'dev',
            'prd'     : 'prd',
            'drop-1'  : 'sit',
            'drop-2'  : 'uat',
            'empty'   : 'unknown'
            ]

        def env = envMap[Util.tagForBranch(branch)]
        return env == null ? 'empty' : env
    }

    static String tagForBranch(String branch) {

        if (branch == null || branch == '' ) {
            return 'empty'
        }

        if (branch == 'develop') {
            return 'dev'
        } else if (branch.startsWith('release/')) {
            return branch.split('/')[1]
        } else if (branch.startsWith('master')) {
            return 'prd'
        } else {
            return 'emtpy'
        }
    }
}
