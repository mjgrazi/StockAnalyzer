package stockanalyzer

import groovyx.net.http.HTTPBuilder

class LoginData {
    static scaffold = true

    String loginToken
    String response
    String name

    static constraints = {
    }

    def retrieveLoginData() {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
        def loginResponse = http.get(path: '/data.asmx/Login', query: [Username: 'mjgrazi', Password: 'test123'])
        this.response = loginResponse.attributes().get 'Message'
        this.loginToken = loginResponse.attributes().get 'Token'
        this.name = "Login"
        this.save()
    }
}
