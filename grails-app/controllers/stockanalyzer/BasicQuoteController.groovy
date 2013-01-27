package stockanalyzer

import org.springframework.dao.DataIntegrityViolationException

class BasicQuoteController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    static scaffold = BasicQuote


    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [basicQuoteInstanceList: BasicQuote.list(params), basicQuoteInstanceTotal: BasicQuote.count()]
    }

    def create() {
        [basicQuoteInstance: new BasicQuote(params)]
    }

    def save() {
        def basicQuoteInstance = new BasicQuote(params)
        if (!basicQuoteInstance.save(flush: true)) {
            render(view: "create", model: [basicQuoteInstance: basicQuoteInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), basicQuoteInstance.id])
        redirect(action: "show", id: basicQuoteInstance.id)
    }

    def show(Long id) {
        def basicQuoteInstance = BasicQuote.get(id)
        if (!basicQuoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), id])
            redirect(action: "list")
            return
        }

        [basicQuoteInstance: basicQuoteInstance]
    }

    def edit(Long id) {
        def basicQuoteInstance = BasicQuote.get(id)
        if (!basicQuoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), id])
            redirect(action: "list")
            return
        }

        [basicQuoteInstance: basicQuoteInstance]
    }

    def update(Long id, Long version) {
        def basicQuoteInstance = BasicQuote.get(id)
        if (!basicQuoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (basicQuoteInstance.version > version) {
                basicQuoteInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'basicQuote.label', default: 'BasicQuote')] as Object[],
                        "Another user has updated this BasicQuote while you were editing")
                render(view: "edit", model: [basicQuoteInstance: basicQuoteInstance])
                return
            }
        }

        basicQuoteInstance.properties = params

        if (!basicQuoteInstance.save(flush: true)) {
            render(view: "edit", model: [basicQuoteInstance: basicQuoteInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), basicQuoteInstance.id])
        redirect(action: "show", id: basicQuoteInstance.id)
    }

    def delete(Long id) {
        def basicQuoteInstance = BasicQuote.get(id)
        if (!basicQuoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), id])
            redirect(action: "list")
            return
        }

        try {
            basicQuoteInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'basicQuote.label', default: 'BasicQuote'), id])
            redirect(action: "show", id: id)
        }
    }

    def retrieveQuote() {
        def basicQuote = new BasicQuote()
        def loginData = new LoginData()
        loginData.getLoginData()
        basicQuote.getSingleQuote(loginData.loginToken, 'NASDAQ', 'AAPL')
    }


}
