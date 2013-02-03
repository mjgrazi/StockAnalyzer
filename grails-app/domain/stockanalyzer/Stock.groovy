package stockanalyzer

import groovyx.net.http.HTTPBuilder

import java.text.SimpleDateFormat

class Stock {

    static scaffold = true
    List quotes = new ArrayList()
    static hasMany = [quotes: BasicQuote]
    String symbol
    String name
    String exchange

    static constraints = {
        symbol(blank: false)
        name(blank: false)
    }

    def retrieve200Quotes(String token, String exchange, String symbol) {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')

        int x = -200;
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, x);
        Date day200Ago = cal.getTime();
        SimpleDateFormat formatInput = new SimpleDateFormat("yyyyMMdd")
        String prevDateString = new String(formatInput.format(day200Ago))

        def loginResponse = http.get(path: '/data.asmx/SymbolHistory', query: [Token: token, Exchange: exchange, Symbol: symbol, StartDate: prevDateString])
        def quoteList = loginResponse.QUOTES[0]


        quoteList.QUOTE.each {
//            if (quotes.size() < 200) {
            BasicQuote quote = new BasicQuote()

            String dateStr = it.attributes().get 'DateTime'
            Date theDate = new Date().parse("yyyy-MM-dd", dateStr)
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy")
            String dateString = new String(dateFormat.format(theDate));
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
            quote.setDateString(dateString)
            quote.setDate(theDate)
            quote.setSymbol(symbol)

            addToQuotes(quote)
//            }
        }
    }

    def retrieveFullQuoteList(String token, String exchange, String symbol) {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')
        def loginResponse = http.get(path: '/data.asmx/SymbolHistory', query: [Token: token, Exchange: exchange, Symbol: symbol, StartDate: "19000101"])
        def quoteList = loginResponse.QUOTES[0]


        quoteList.QUOTE.each {
            BasicQuote quote = new BasicQuote()

            String dateStr = it.attributes().get 'DateTime'
            Date theDate = new Date().parse("yyyy-MM-dd", dateStr)
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy")
            String dateString = new String(dateFormat.format(theDate));
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
            quote.setDateString(dateString)
            quote.setSymbol(symbol)

            addToQuotes(quote)
        }
    }
}
