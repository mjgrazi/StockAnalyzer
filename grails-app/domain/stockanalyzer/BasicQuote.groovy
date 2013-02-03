package stockanalyzer

import groovyx.net.http.HTTPBuilder

class BasicQuote {

    static scaffold = true

    String symbol
    BigDecimal open
    BigDecimal high
    BigDecimal low
    BigDecimal close
    BigInteger volume
    String dateString
    Date date

    static constraints = {
    }

    def void getLatestQuote(String token, String exchange, String symbol) {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')

        def loginResponse = http.get(path: '/data.asmx/QuoteGet', query: [Token: token, Exchange: exchange, Symbol: this.symbol])
        def testStr
//        setOpen(BigDecimal(loginResponse.attributes().get 'Open')
    }
}
