package xyz.rickygao.dishow.controller

import io.javalin.Context

object ExceptionController {

    fun <T> badRequest(exception: T, ctx: Context) {
        ctx.status(400)
    }

    fun <T> notFound(exception: T, ctx: Context) {
        ctx.status(404)
    }

}