import grails.converters.JSON
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
    }
    def destroy = {
    }
}
