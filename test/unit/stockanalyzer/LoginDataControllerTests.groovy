package stockanalyzer



import org.junit.*
import grails.test.mixin.*

@TestFor(LoginDataController)
@Mock(LoginData)
class LoginDataControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/loginData/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.loginDataInstanceList.size() == 0
        assert model.loginDataInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.loginDataInstance != null
    }

    void testSave() {
        controller.save()

        assert model.loginDataInstance != null
        assert view == '/loginData/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/loginData/show/1'
        assert controller.flash.message != null
        assert LoginData.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/loginData/list'

        populateValidParams(params)
        def loginData = new LoginData(params)

        assert loginData.save() != null

        params.id = loginData.id

        def model = controller.show()

        assert model.loginDataInstance == loginData
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/loginData/list'

        populateValidParams(params)
        def loginData = new LoginData(params)

        assert loginData.save() != null

        params.id = loginData.id

        def model = controller.edit()

        assert model.loginDataInstance == loginData
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/loginData/list'

        response.reset()

        populateValidParams(params)
        def loginData = new LoginData(params)

        assert loginData.save() != null

        // test invalid parameters in update
        params.id = loginData.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/loginData/edit"
        assert model.loginDataInstance != null

        loginData.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/loginData/show/$loginData.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        loginData.clearErrors()

        populateValidParams(params)
        params.id = loginData.id
        params.version = -1
        controller.update()

        assert view == "/loginData/edit"
        assert model.loginDataInstance != null
        assert model.loginDataInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/loginData/list'

        response.reset()

        populateValidParams(params)
        def loginData = new LoginData(params)

        assert loginData.save() != null
        assert LoginData.count() == 1

        params.id = loginData.id

        controller.delete()

        assert LoginData.count() == 0
        assert LoginData.get(loginData.id) == null
        assert response.redirectedUrl == '/loginData/list'
    }
}
