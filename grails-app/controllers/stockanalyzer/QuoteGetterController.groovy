package stockanalyzer

import org.springframework.dao.DataIntegrityViolationException

class QuoteGetterController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [quoteGetterInstanceList: QuoteGetter.list(params), quoteGetterInstanceTotal: QuoteGetter.count()]
    }

    def create() {
        [quoteGetterInstance: new QuoteGetter(params)]
    }

    def save() {
        def quoteGetterInstance = new QuoteGetter(params)
        if (!quoteGetterInstance.save(flush: true)) {
            render(view: "create", model: [quoteGetterInstance: quoteGetterInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), quoteGetterInstance.id])
        redirect(action: "show", id: quoteGetterInstance.id)
    }

    def show(Long id) {
        def quoteGetterInstance = QuoteGetter.get(id)
        if (!quoteGetterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), id])
            redirect(action: "list")
            return
        }

        [quoteGetterInstance: quoteGetterInstance]
    }

    def edit(Long id) {
        def quoteGetterInstance = QuoteGetter.get(id)
        if (!quoteGetterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), id])
            redirect(action: "list")
            return
        }

        [quoteGetterInstance: quoteGetterInstance]
    }

    def update(Long id, Long version) {
        def quoteGetterInstance = QuoteGetter.get(id)
        if (!quoteGetterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (quoteGetterInstance.version > version) {
                quoteGetterInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'quoteGetter.label', default: 'QuoteGetter')] as Object[],
                        "Another user has updated this QuoteGetter while you were editing")
                render(view: "edit", model: [quoteGetterInstance: quoteGetterInstance])
                return
            }
        }

        quoteGetterInstance.properties = params

        if (!quoteGetterInstance.save(flush: true)) {
            render(view: "edit", model: [quoteGetterInstance: quoteGetterInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), quoteGetterInstance.id])
        redirect(action: "show", id: quoteGetterInstance.id)
    }

    def delete(Long id) {
        def quoteGetterInstance = QuoteGetter.get(id)
        if (!quoteGetterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), id])
            redirect(action: "list")
            return
        }

        try {
            quoteGetterInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'quoteGetter.label', default: 'QuoteGetter'), id])
            redirect(action: "show", id: id)
        }
    }

    def retrieveHist(GString symbol, Date date) {

    }
}
