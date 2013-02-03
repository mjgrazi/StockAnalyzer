import grails.converters.JSON
import stockanalyzer.BasicQuote
import stockanalyzer.Exchange
import stockanalyzer.ExchangeList
import stockanalyzer.Stock

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(ExchangeList) {
            def returnArray = [:]
            returnArray['exchanges'] = it.exchanges
            return returnArray
        }
        JSON.registerObjectMarshaller(Exchange) {
            def returnArray = [:]
            returnArray['name'] = it.name
            returnArray['code'] = it.code
            returnArray['symbols'] = it.symbols
            return returnArray
        }
        JSON.registerObjectMarshaller(Stock) {
            def returnArray = [:]
            returnArray['name'] = it.name
            returnArray['symbol'] = it.symbol
            return returnArray
        }
        JSON.registerObjectMarshaller(BasicQuote) {
            def returnArray = [:]
            returnArray['date'] = it.date
            returnArray['open'] = it.open
            returnArray['close'] = it.close
            returnArray['high'] = it.high
            returnArray['low'] = it.low
            returnArray['volume'] = it.volume
            return returnArray
        }
    }
    def destroy = {
    }
}
