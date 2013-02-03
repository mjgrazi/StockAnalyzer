package stockanalyzer



import org.junit.*
import grails.test.mixin.*

@TestFor(TestDatabaseController)
@Mock(TestDatabase)
class TestDatabaseControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/testDatabase/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.testDatabaseInstanceList.size() == 0
        assert model.testDatabaseInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.testDatabaseInstance != null
    }

    void testSave() {
        controller.save()

        assert model.testDatabaseInstance != null
        assert view == '/testDatabase/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/testDatabase/show/1'
        assert controller.flash.message != null
        assert TestDatabase.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/testDatabase/list'

        populateValidParams(params)
        def testDatabase = new TestDatabase(params)

        assert testDatabase.save() != null

        params.id = testDatabase.id

        def model = controller.show()

        assert model.testDatabaseInstance == testDatabase
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/testDatabase/list'

        populateValidParams(params)
        def testDatabase = new TestDatabase(params)

        assert testDatabase.save() != null

        params.id = testDatabase.id

        def model = controller.edit()

        assert model.testDatabaseInstance == testDatabase
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/testDatabase/list'

        response.reset()

        populateValidParams(params)
        def testDatabase = new TestDatabase(params)

        assert testDatabase.save() != null

        // test invalid parameters in update
        params.id = testDatabase.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/testDatabase/edit"
        assert model.testDatabaseInstance != null

        testDatabase.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/testDatabase/show/$testDatabase.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        testDatabase.clearErrors()

        populateValidParams(params)
        params.id = testDatabase.id
        params.version = -1
        controller.update()

        assert view == "/testDatabase/edit"
        assert model.testDatabaseInstance != null
        assert model.testDatabaseInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/testDatabase/list'

        response.reset()

        populateValidParams(params)
        def testDatabase = new TestDatabase(params)

        assert testDatabase.save() != null
        assert TestDatabase.count() == 1

        params.id = testDatabase.id

        controller.delete()

        assert TestDatabase.count() == 0
        assert TestDatabase.get(testDatabase.id) == null
        assert response.redirectedUrl == '/testDatabase/list'
    }
}
