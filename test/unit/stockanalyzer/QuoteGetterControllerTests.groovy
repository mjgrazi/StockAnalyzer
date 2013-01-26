package stockanalyzer



import org.junit.*
import grails.test.mixin.*

@TestFor(QuoteGetterController)
@Mock(QuoteGetter)
class QuoteGetterControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/quoteGetter/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.quoteGetterInstanceList.size() == 0
        assert model.quoteGetterInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.quoteGetterInstance != null
    }

    void testSave() {
        controller.save()

        assert model.quoteGetterInstance != null
        assert view == '/quoteGetter/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/quoteGetter/show/1'
        assert controller.flash.message != null
        assert QuoteGetter.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/quoteGetter/list'

        populateValidParams(params)
        def quoteGetter = new QuoteGetter(params)

        assert quoteGetter.save() != null

        params.id = quoteGetter.id

        def model = controller.show()

        assert model.quoteGetterInstance == quoteGetter
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/quoteGetter/list'

        populateValidParams(params)
        def quoteGetter = new QuoteGetter(params)

        assert quoteGetter.save() != null

        params.id = quoteGetter.id

        def model = controller.edit()

        assert model.quoteGetterInstance == quoteGetter
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/quoteGetter/list'

        response.reset()

        populateValidParams(params)
        def quoteGetter = new QuoteGetter(params)

        assert quoteGetter.save() != null

        // test invalid parameters in update
        params.id = quoteGetter.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/quoteGetter/edit"
        assert model.quoteGetterInstance != null

        quoteGetter.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/quoteGetter/show/$quoteGetter.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        quoteGetter.clearErrors()

        populateValidParams(params)
        params.id = quoteGetter.id
        params.version = -1
        controller.update()

        assert view == "/quoteGetter/edit"
        assert model.quoteGetterInstance != null
        assert model.quoteGetterInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/quoteGetter/list'

        response.reset()

        populateValidParams(params)
        def quoteGetter = new QuoteGetter(params)

        assert quoteGetter.save() != null
        assert QuoteGetter.count() == 1

        params.id = quoteGetter.id

        controller.delete()

        assert QuoteGetter.count() == 0
        assert QuoteGetter.get(quoteGetter.id) == null
        assert response.redirectedUrl == '/quoteGetter/list'
    }
}
