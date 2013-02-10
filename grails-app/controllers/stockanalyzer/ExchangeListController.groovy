package stockanalyzer

import grails.converters.JSON
import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class ExchangeListController {

    static scaffold = ExchangeList
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [exchangeListInstanceList: ExchangeList.list(params), exchangeListInstanceTotal: ExchangeList.count()]
    }

    def create() {
        [exchangeListInstance: new ExchangeList(params)]
    }

    def save() {
        def exchangeListInstance = new ExchangeList(params)
        if (!exchangeListInstance.save(flush: true)) {
            render(view: "create", model: [exchangeListInstance: exchangeListInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), exchangeListInstance.id])
        redirect(action: "show", id: exchangeListInstance.id)
    }

    def show(Long id) {
        def exchangeListInstance = ExchangeList.get(id)
        if (!exchangeListInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), id])
            redirect(action: "list")
            return
        }

        [exchangeListInstance: exchangeListInstance]
    }

    def edit(Long id) {
        def exchangeListInstance = ExchangeList.get(id)
        if (!exchangeListInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), id])
            redirect(action: "list")
            return
        }

        [exchangeListInstance: exchangeListInstance]
    }

    def update(Long id, Long version) {
        def exchangeListInstance = ExchangeList.get(id)
        if (!exchangeListInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (exchangeListInstance.version > version) {
                exchangeListInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'exchangeList.label', default: 'ExchangeList')] as Object[],
                        "Another user has updated this ExchangeList while you were editing")
                render(view: "edit", model: [exchangeListInstance: exchangeListInstance])
                return
            }
        }

        exchangeListInstance.properties = params

        if (!exchangeListInstance.save(flush: true)) {
            render(view: "edit", model: [exchangeListInstance: exchangeListInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), exchangeListInstance.id])
        redirect(action: "show", id: exchangeListInstance.id)
    }

    def delete(Long id) {
        def exchangeListInstance = ExchangeList.get(id)
        if (!exchangeListInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), id])
            redirect(action: "list")
            return
        }

        try {
            exchangeListInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'exchangeList.label', default: 'ExchangeList'), id])
            redirect(action: "show", id: id)
        }
    }

    def getStockList() {
        DataGetterService service = new DataGetterService()
        render service.getStockListForExchange(params.id) as JSON
    }

    def getJSON() {
        render ExchangeList.list() as JSON
    }

    def getXML() {
        render ExchangeList.list() as XML
    }
}
