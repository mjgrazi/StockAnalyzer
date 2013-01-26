package stockanalyzer



import org.junit.*
import grails.test.mixin.*

@TestFor(ExchangeListController)
@Mock(ExchangeList)
class ExchangeListControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/exchangeList/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.exchangeListInstanceList.size() == 0
        assert model.exchangeListInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.exchangeListInstance != null
    }

    void testSave() {
        controller.save()

        assert model.exchangeListInstance != null
        assert view == '/exchangeList/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/exchangeList/show/1'
        assert controller.flash.message != null
        assert ExchangeList.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/exchangeList/list'

        populateValidParams(params)
        def exchangeList = new ExchangeList(params)

        assert exchangeList.save() != null

        params.id = exchangeList.id

        def model = controller.show()

        assert model.exchangeListInstance == exchangeList
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/exchangeList/list'

        populateValidParams(params)
        def exchangeList = new ExchangeList(params)

        assert exchangeList.save() != null

        params.id = exchangeList.id

        def model = controller.edit()

        assert model.exchangeListInstance == exchangeList
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/exchangeList/list'

        response.reset()

        populateValidParams(params)
        def exchangeList = new ExchangeList(params)

        assert exchangeList.save() != null

        // test invalid parameters in update
        params.id = exchangeList.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/exchangeList/edit"
        assert model.exchangeListInstance != null

        exchangeList.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/exchangeList/show/$exchangeList.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        exchangeList.clearErrors()

        populateValidParams(params)
        params.id = exchangeList.id
        params.version = -1
        controller.update()

        assert view == "/exchangeList/edit"
        assert model.exchangeListInstance != null
        assert model.exchangeListInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/exchangeList/list'

        response.reset()

        populateValidParams(params)
        def exchangeList = new ExchangeList(params)

        assert exchangeList.save() != null
        assert ExchangeList.count() == 1

        params.id = exchangeList.id

        controller.delete()

        assert ExchangeList.count() == 0
        assert ExchangeList.get(exchangeList.id) == null
        assert response.redirectedUrl == '/exchangeList/list'
    }
}
