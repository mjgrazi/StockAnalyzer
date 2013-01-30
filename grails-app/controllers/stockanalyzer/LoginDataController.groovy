package stockanalyzer

import org.springframework.dao.DataIntegrityViolationException

class LoginDataController {
    static scaffold = LoginData

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [loginDataInstanceList: LoginData.list(params), loginDataInstanceTotal: LoginData.count()]
    }

    def create() {
        [loginDataInstance: new LoginData(params)]
    }

    def save() {
        def loginDataInstance = new LoginData(params)
        if (!loginDataInstance.save(flush: true)) {
            render(view: "create", model: [loginDataInstance: loginDataInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'loginData.label', default: 'LoginData'), loginDataInstance.id])
        redirect(action: "show", id: loginDataInstance.id)
    }

    def show(Long id) {
        def loginDataInstance = LoginData.get(id)
        if (!loginDataInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'loginData.label', default: 'LoginData'), id])
            redirect(action: "list")
            return
        }

        [loginDataInstance: loginDataInstance]
    }

    def edit(Long id) {
        def loginDataInstance = LoginData.get(id)
        if (!loginDataInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'loginData.label', default: 'LoginData'), id])
            redirect(action: "list")
            return
        }

        [loginDataInstance: loginDataInstance]
    }

    def update(Long id, Long version) {
        def loginDataInstance = LoginData.get(id)
        if (!loginDataInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'loginData.label', default: 'LoginData'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (loginDataInstance.version > version) {
                loginDataInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'loginData.label', default: 'LoginData')] as Object[],
                        "Another user has updated this LoginData while you were editing")
                render(view: "edit", model: [loginDataInstance: loginDataInstance])
                return
            }
        }

        loginDataInstance.properties = params

        if (!loginDataInstance.save(flush: true)) {
            render(view: "edit", model: [loginDataInstance: loginDataInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'loginData.label', default: 'LoginData'), loginDataInstance.id])
        redirect(action: "show", id: loginDataInstance.id)
    }

    def delete(Long id) {
        def loginDataInstance = LoginData.get(id)
        if (!loginDataInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'loginData.label', default: 'LoginData'), id])
            redirect(action: "list")
            return
        }

        try {
            loginDataInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'loginData.label', default: 'LoginData'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'loginData.label', default: 'LoginData'), id])
            redirect(action: "show", id: id)
        }
    }

    def doLogin() {
        if (!LoginData.findByName("Login")) {
            LoginData loginData = new LoginData()
            loginData.retrieveLoginData()
            render("Login Token: " + loginData.loginToken)
        } else {
            render("Login already exists")
        }

    }
}
