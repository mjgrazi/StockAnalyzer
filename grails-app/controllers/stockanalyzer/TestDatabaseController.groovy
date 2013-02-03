package stockanalyzer

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException

class TestDatabaseController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "run", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [testDatabaseInstanceList: TestDatabase.list(params), testDatabaseInstanceTotal: TestDatabase.count()]
    }

    def create() {
        [testDatabaseInstance: new TestDatabase(params)]
    }

    def save() {
        def testDatabaseInstance = new TestDatabase(params)
        if (!testDatabaseInstance.save(flush: true)) {
            render(view: "create", model: [testDatabaseInstance: testDatabaseInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), testDatabaseInstance.id])
        redirect(action: "show", id: testDatabaseInstance.id)
    }

    def show(Long id) {
        def testDatabaseInstance = TestDatabase.get(id)
        if (!testDatabaseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), id])
            redirect(action: "list")
            return
        }

        [testDatabaseInstance: testDatabaseInstance]
    }

    def edit(Long id) {
        def testDatabaseInstance = TestDatabase.get(id)
        if (!testDatabaseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), id])
            redirect(action: "list")
            return
        }

        [testDatabaseInstance: testDatabaseInstance]
    }

    def update(Long id, Long version) {
        def testDatabaseInstance = TestDatabase.get(id)
        if (!testDatabaseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (testDatabaseInstance.version > version) {
                testDatabaseInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'testDatabase.label', default: 'TestDatabase')] as Object[],
                        "Another user has updated this TestDatabase while you were editing")
                render(view: "edit", model: [testDatabaseInstance: testDatabaseInstance])
                return
            }
        }

        testDatabaseInstance.properties = params

        if (!testDatabaseInstance.save(flush: true)) {
            render(view: "edit", model: [testDatabaseInstance: testDatabaseInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), testDatabaseInstance.id])
        redirect(action: "show", id: testDatabaseInstance.id)
    }

    def delete(Long id) {
        def testDatabaseInstance = TestDatabase.get(id)
        if (!testDatabaseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), id])
            redirect(action: "list")
            return
        }

        try {
            testDatabaseInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'testDatabase.label', default: 'TestDatabase'), id])
            redirect(action: "show", id: id)
        }
    }

    def run() {
        if (!ExchangeList.findByName("Exchange List")) {
            ExchangeList exchangeList = new ExchangeList()
            exchangeList.retrieveExchangeList()
            exchangeList.setName("Exchange List")
            exchangeList.save()
        } else
            render "Exchange list already loaded"


    }

    def loadExchange() {
        String input = params.id
        input = input.toUpperCase()

        if (Exchange.findByCode(input)) {
            Exchange exchange = Exchange.findByCode(input)
            if (exchange.symbols.size() < 1) {
                exchange.retrieveStockList(exchange.code)
                render("Loaded stock list for " + exchange.name)
            } else render("Exchange already loaded")
        } else render("Invalid exchange code")
    }

    def getter() {
        def quoteList
        String input = params.id
        input = input.toUpperCase()
        Stock stock = Stock.findBySymbol(input)
        if (stock) {
            if (stock.quotes.size() < 1) {
                stock.retrieveFullQuoteList(LoginData.findByName('Login').loginToken, stock.exchange, stock.symbol)
                quoteList = stock.quotes
            } else {
                quoteList = BasicQuote.findAllBySymbol(input)
            }
        }
        JSON.registerObjectMarshaller(Stock) {
            def returnArray = [:]
            returnArray['name'] = it.name
            returnArray['symbol'] = it.symbol
            returnArray['quotes'] = it.quotes
            return returnArray
        }
        render stock as JSON
        JSON.registerObjectMarshaller(Stock) {
            def returnArray = [:]
            returnArray['name'] = it.name
            returnArray['symbol'] = it.symbol
            return returnArray
        }
    }

    def exchangePopulator() {
        List exchangeList = Exchange.findAll()
        exchangeList.each { exchange ->
            exchange.retrieveStockList(exchange.code)
            println exchange.name
            exchange.save()
        }
    }
}

