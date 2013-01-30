package stockanalyzer

import groovyx.net.http.HTTPBuilder

class ExchangeList {

    static scaffold = true
    static hasMany = [exchanges: Exchange]

    String name

    static constraints = {
    }

    def retrieveExchangeList() {
        LoginData loginData = LoginData.findByName("Login")
        if (loginData) {
            HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
            def loginResponse = http.get(path: '/data.asmx/ExchangeList', query: [Token: loginData.loginToken])
            def firstRecord = loginResponse.EXCHANGES[0]
            String message = loginResponse.attributes().get "Message"
            if (message.equals("Success")) {
                for (it in firstRecord.EXCHANGE) {
                    Exchange tempExchange = new Exchange()
                    String tempNameString = it.attributes().get 'Name'
                    String tempCodeString = it.attributes().get 'Code'
                    if (tempCodeString.equals("NASDAQ") || tempCodeString.equals("NYSE")) {
                        tempExchange.setName(tempNameString)
                        tempExchange.setCode(tempCodeString)
                        tempExchange.retrieveStockList(tempCodeString)
                        addToExchanges(tempExchange)
                        println(tempExchange.name + " added to database")
                    }
                }
            } else {
                LoginData delLogin = LoginData.findByName("Login")
                delLogin.delete()
                LoginData newLogin = new LoginData()
                newLogin.retrieveLoginData()
                retrieveExchangeList()
            }
        } else {
            LoginData newLogin = new LoginData()
            newLogin.retrieveLoginData()
            retrieveExchangeList()
        }
    }

}
