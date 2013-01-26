package stockanalyzer



import org.junit.*
import grails.test.mixin.*

@TestFor(ExchangeController)
@Mock(Exchange)
class ExchangeControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/exchange/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.exchangeInstanceList.size() == 0
        assert model.exchangeInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.exchangeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.exchangeInstance != null
        assert view == '/exchange/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/exchange/show/1'
        assert controller.flash.message != null
        assert Exchange.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/exchange/list'

        populateValidParams(params)
        def exchange = new Exchange(params)

        assert exchange.save() != null

        params.id = exchange.id

        def model = controller.show()

        assert model.exchangeInstance == exchange
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/exchange/list'

        populateValidParams(params)
        def exchange = new Exchange(params)

        assert exchange.save() != null

        params.id = exchange.id

        def model = controller.edit()

        assert model.exchangeInstance == exchange
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/exchange/list'

        response.reset()

        populateValidParams(params)
        def exchange = new Exchange(params)

        assert exchange.save() != null

        // test invalid parameters in update
        params.id = exchange.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/exchange/edit"
        assert model.exchangeInstance != null

        exchange.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/exchange/show/$exchange.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        exchange.clearErrors()

        populateValidParams(params)
        params.id = exchange.id
        params.version = -1
        controller.update()

        assert view == "/exchange/edit"
        assert model.exchangeInstance != null
        assert model.exchangeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/exchange/list'

        response.reset()

        populateValidParams(params)
        def exchange = new Exchange(params)

        assert exchange.save() != null
        assert Exchange.count() == 1

        params.id = exchange.id

        controller.delete()

        assert Exchange.count() == 0
        assert Exchange.get(exchange.id) == null
        assert response.redirectedUrl == '/exchange/list'
    }
}
