package stockanalyzer

import groovyx.net.http.HTTPBuilder

class DataGetterService {

    def serviceMethod() {

    }

    def retrieveLoginData() {
        if (!LoginData.findByName("Login")) {
            LoginData loginData = new LoginData()

            HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
            def loginResponse = http.get(path: '/data.asmx/Login', query: [Username: 'mjgrazi', Password: 'test123'])
            loginData.response = loginResponse.attributes().get 'Message'
            loginData.loginToken = loginResponse.attributes().get 'Token'
            loginData.name = "Login"
            loginData.save()
        }
    }

    def retrieveExchangeList() {
        if (!ExchangeList.findByName("Exchange List")) {
            ExchangeList exchangeList = new ExchangeList()
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
                        tempExchange.setName(tempNameString)
                        tempExchange.setCode(tempCodeString)

                        exchangeList.addToExchanges(tempExchange)
                        println(tempExchange.name + " added to database")
                    }
                } else {
                    LoginData delLogin = LoginData.findByName("Login")
                    delLogin.delete()
                    retrieveLoginData()
                    retrieveExchangeList()
                }
            } else {
                LoginData newLogin = new LoginData()
                newLogin.retrieveLoginData()
                retrieveExchangeList()
            }
            exchangeList.setName("Exchange List")
            exchangeList.save()
        }
    }

    def retrieveExchange(String exchangeCode) {
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
                    Exchange exchange = new Exchange()
                    exchange.addToSymbols(tempStock)
                }
            } else {
                LoginData delLogin = LoginData.findByName("Login")
                delLogin.delete()
                retrieveLoginData()
                retrieveExchangeList()
            }
        } else {
            LoginData newLogin = new LoginData()
            newLogin.retrieveLoginData()
            retrieveExchangeList()
        }
    }

    def getStockListForExchange(String exchangeCode) {
        if (Exchange.findByCode(exchangeCode)) {
            Exchange exchange = Exchange.findByCode(exchangeCode)
            exchange.retrieveStockList(exchange.code)
            exchange.save()
        }
    }

    def getExchangeListJSON() {
        ExchangeList exchangeList = ExchangeList.findByName("Exchange List")
    }

}
