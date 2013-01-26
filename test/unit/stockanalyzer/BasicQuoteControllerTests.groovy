package stockanalyzer



import org.junit.*
import grails.test.mixin.*

@TestFor(BasicQuoteController)
@Mock(BasicQuote)
class BasicQuoteControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/basicQuote/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.basicQuoteInstanceList.size() == 0
        assert model.basicQuoteInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.basicQuoteInstance != null
    }

    void testSave() {
        controller.save()

        assert model.basicQuoteInstance != null
        assert view == '/basicQuote/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/basicQuote/show/1'
        assert controller.flash.message != null
        assert BasicQuote.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/basicQuote/list'

        populateValidParams(params)
        def basicQuote = new BasicQuote(params)

        assert basicQuote.save() != null

        params.id = basicQuote.id

        def model = controller.show()

        assert model.basicQuoteInstance == basicQuote
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/basicQuote/list'

        populateValidParams(params)
        def basicQuote = new BasicQuote(params)

        assert basicQuote.save() != null

        params.id = basicQuote.id

        def model = controller.edit()

        assert model.basicQuoteInstance == basicQuote
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/basicQuote/list'

        response.reset()

        populateValidParams(params)
        def basicQuote = new BasicQuote(params)

        assert basicQuote.save() != null

        // test invalid parameters in update
        params.id = basicQuote.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/basicQuote/edit"
        assert model.basicQuoteInstance != null

        basicQuote.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/basicQuote/show/$basicQuote.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        basicQuote.clearErrors()

        populateValidParams(params)
        params.id = basicQuote.id
        params.version = -1
        controller.update()

        assert view == "/basicQuote/edit"
        assert model.basicQuoteInstance != null
        assert model.basicQuoteInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/basicQuote/list'

        response.reset()

        populateValidParams(params)
        def basicQuote = new BasicQuote(params)

        assert basicQuote.save() != null
        assert BasicQuote.count() == 1

        params.id = basicQuote.id

        controller.delete()

        assert BasicQuote.count() == 0
        assert BasicQuote.get(basicQuote.id) == null
        assert response.redirectedUrl == '/basicQuote/list'
    }
}
