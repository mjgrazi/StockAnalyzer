package stockanalyzer

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException

class ExchangeController {

    static scaffold = Exchange

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [exchangeInstanceList: Exchange.list(params), exchangeInstanceTotal: Exchange.count()]
    }

    def create() {
        [exchangeInstance: new Exchange(params)]
    }

    def save() {
        def exchangeInstance = new Exchange(params)
        if (!exchangeInstance.save(flush: true)) {
            render(view: "create", model: [exchangeInstance: exchangeInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'exchange.label', default: 'Exchange'), exchangeInstance.id])
        redirect(action: "show", id: exchangeInstance.id)
    }

    def show(Long id) {
        def exchangeInstance = Exchange.get(id)
        if (!exchangeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchange.label', default: 'Exchange'), id])
            redirect(action: "list")
            return
        }

        [exchangeInstance: exchangeInstance]
    }

    def edit(Long id) {
        def exchangeInstance = Exchange.get(id)
        if (!exchangeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchange.label', default: 'Exchange'), id])
            redirect(action: "list")
            return
        }

        [exchangeInstance: exchangeInstance]
    }

    def update(Long id, Long version) {
        def exchangeInstance = Exchange.get(id)
        if (!exchangeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchange.label', default: 'Exchange'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (exchangeInstance.version > version) {
                exchangeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'exchange.label', default: 'Exchange')] as Object[],
                        "Another user has updated this Exchange while you were editing")
                render(view: "edit", model: [exchangeInstance: exchangeInstance])
                return
            }
        }

        exchangeInstance.properties = params

        if (!exchangeInstance.save(flush: true)) {
            render(view: "edit", model: [exchangeInstance: exchangeInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'exchange.label', default: 'Exchange'), exchangeInstance.id])
        redirect(action: "show", id: exchangeInstance.id)
    }

    def delete(Long id) {
        def exchangeInstance = Exchange.get(id)
        if (!exchangeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'exchange.label', default: 'Exchange'), id])
            redirect(action: "list")
            return
        }

        try {
            exchangeInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'exchange.label', default: 'Exchange'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'exchange.label', default: 'Exchange'), id])
            redirect(action: "show", id: id)
        }
    }

    def stockList() {
        Exchange exchange = Exchange.findByCode(params.id)
        if (exchange) {
            if (exchange.symbols.size() < 1)
                exchange.retrieveStockList(exchange.code)
            render exchange as JSON
        } else
            render "Exchange not found"
    }

}
