package stockanalyzer

import groovyx.net.http.HTTPBuilder

class ExchangeList {

    static scaffold = true
    static hasMany = [exchanges:Exchange]

    String name

    static constraints = {
    }

    def retrieveExchangeList() {
        LoginData loginData = new LoginData()
        loginData.retrieveLoginData()
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
        def loginResponse = http.get(path: '/data.asmx/ExchangeList', query: [Token: loginData.loginToken])
        def firstRecord = loginResponse.EXCHANGES[0]


        for (it in firstRecord.EXCHANGE) {
            Exchange tempExchange = new Exchange()
            String tempNameString = it.attributes().get 'Name'
            String tempCodeString = it.attributes().get 'Code'

            if (tempCodeString.equals("NASDAQ")) {

                tempExchange.setName(tempNameString)
                tempExchange.setCode(tempCodeString)

                tempExchange.retrieveStockList(loginData.loginToken, tempCodeString)

                addToExchanges(tempExchange)
            }
        }
    }

}
