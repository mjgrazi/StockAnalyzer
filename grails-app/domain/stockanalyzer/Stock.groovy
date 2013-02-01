package stockanalyzer

import groovyx.net.http.HTTPBuilder

class Stock {

    static scaffold = true
    List quotes = new ArrayList()
    static hasMany = [quotes: BasicQuote]
    String symbol
    String name

    static constraints = {
        symbol(blank: false)
        name(blank: false)
    }

    def retrieveFullQuoteList(String token, String exchange, String symbol) {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
        def loginResponse = http.get(path: '/data.asmx/SymbolHistory', query: [Token: token, Exchange: exchange, Symbol: symbol, StartDate: "19000101"])
        def quoteList = loginResponse.QUOTES[0]


        quoteList.QUOTE.each {
            BasicQuote quote = new BasicQuote()

            String dateStr = it.attributes().get 'DateTime'
            Date theDate = new Date().parse("yyyy-MM-dd", dateStr)
            BigDecimal tempOpen = new BigDecimal(it.attributes().get('Open'))
            BigDecimal tempClose = new BigDecimal(it.attributes().get('Close'))
            BigDecimal tempHigh = new BigDecimal(it.attributes().get('High'))
            BigDecimal tempLow = new BigDecimal(it.attributes().get('Low'))
            BigInteger tempVolume = new BigInteger(it.attributes().get('Volume'))

            quote.setOpen(tempOpen)
            quote.setClose(tempClose)
            quote.setHigh(tempHigh)
            quote.setLow(tempLow)
            quote.setVolume(tempVolume)
            quote.setDate(theDate)
            quote.setSymbol(symbol)

            addToQuotes(quote)
        }
    }
}
