package stockanalyzer

import groovyx.net.http.HTTPBuilder

class BasicQuote {

    public GString symbol
    BigDecimal open
    BigDecimal high
    BigDecimal low
    BigDecimal close
    int volume
    public Date date

    static constraints = {
    }

    def void getSingleQuote(String token, String exchange, String symbol) {
        HTTPBuilder http = new HTTPBuilder('http://ws.eoddata.com')

        def loginResponse = http.get(path: '/data.asmx/QuoteGet', query: [Token: token, Exchange: exchange, Symbol: symbol])
        def testStr
//        setOpen(BigDecimal(loginResponse.attributes().get 'Open')
    }
}
