package stockanalyzer

import groovyx.net.http.HTTPBuilder

class LoginData {
//    static scaffold = true

    String loginToken

    static constraints = {
    }

    def retrieveLoginData() {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
        def loginResponse = http.get(path: '/data.asmx/Login', query: [Username: 'mjgrazi', Password: 'test123'])
        this.loginToken = loginResponse.attributes().get 'Token'
    }

}
