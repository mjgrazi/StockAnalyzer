package stockanalyzer

import groovyx.net.http.HTTPBuilder

class Exchange {

    static scaffold = true

    String name
    String code
    static hasMany = [stocks: Stock]

    static constraints = {
        name(blank: false)
        code(blank: false)
    }

    def retrieveStockList(String token, String exchangeCode) {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
        def loginResponse = http.get(path: '/data.asmx/SymbolList', query: [Token: token, Exchange: exchangeCode])
        def symbolList = loginResponse.SYMBOLS[0]

        symbolList.SYMBOL.each {
            Stock tempStock = new Stock()
            String tempCodeString = it.attributes().get 'Code'
            String tempNameString = it.attributes().get 'Name'
            tempStock.setName(tempNameString)
            tempStock.setCode(tempCodeString)

            if (tempCodeString.equals("AAPL")) {
                tempStock.retrieveFullQuoteList(token, exchangeCode, tempCodeString)
            }
            addToStocks(tempStock)
        }
    }

}
