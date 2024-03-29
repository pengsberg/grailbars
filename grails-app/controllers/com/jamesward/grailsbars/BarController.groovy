package com.jamesward.grailsbars



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BarController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Bar.list(params), model:[barInstanceCount: Bar.count()]
    }

    def show(Bar barInstance) {
        respond barInstance
    }

    def create() {
        respond new Bar(params)
    }

    @Transactional
    def save(Bar barInstance) {
        if (barInstance == null) {
            notFound()
            return
        }

        if (barInstance.hasErrors()) {
            respond barInstance.errors, view:'create'
            return
        }

        barInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'barInstance.label', default: 'Bar'), barInstance.id])
                redirect barInstance
            }
            '*' { respond barInstance, [status: CREATED] }
        }
    }

    def edit(Bar barInstance) {
        respond barInstance
    }

    @Transactional
    def update(Bar barInstance) {
        if (barInstance == null) {
            notFound()
            return
        }

        if (barInstance.hasErrors()) {
            respond barInstance.errors, view:'edit'
            return
        }

        barInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Bar.label', default: 'Bar'), barInstance.id])
                redirect barInstance
            }
            '*'{ respond barInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Bar barInstance) {

        if (barInstance == null) {
            notFound()
            return
        }

        barInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Bar.label', default: 'Bar'), barInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'barInstance.label', default: 'Bar'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
