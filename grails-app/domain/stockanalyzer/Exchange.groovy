package stockanalyzer

import groovyx.net.http.HTTPBuilder

class Exchange {

    static scaffold = true

    String name
    String code
    List symbols = new ArrayList()
    static hasMany = [symbols: Stock]

    static constraints = {
        name(blank: false)
        code(blank: false)
    }

    def retrieveStockList(String exchangeCode) {
        LoginData loginData = LoginData.findByName("Login")
        if (loginData) {
            HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
            def loginResponse = http.get(path: '/data.asmx/SymbolList', query: [Token: loginData.loginToken, Exchange: exchangeCode])
            String message = loginResponse.attributes().get 'Message'
            if (message.equals("Success")) {
                def symbolList = loginResponse.SYMBOLS[0]
                symbolList.SYMBOL.each {
                    Stock tempStock = new Stock()
                    String tempCodeString = it.attributes().get 'Code'
                    String tempNameString = it.attributes().get 'Name'
                    tempStock.setName(tempNameString)
                    tempStock.setSymbol(tempCodeString)
                    tempStock.setExchange(exchangeCode)
                    addToSymbols(tempStock)
                }
            } else {
                generateLoginToken()
                retrieveStockList(exchangeCode)

            }
        } else {
            generateLoginToken()
            retrieveStockList(exchangeCode)
        }

    }

    def generateLoginToken() {
        if (LoginData.findByName("Login")) {
            LoginData data = LoginData.findByName("Login")
            data.delete()
        }
        LoginData data = new LoginData()
        data.retrieveLoginData()
    }

}
