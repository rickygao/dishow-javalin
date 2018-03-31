package xyz.rickygao.dishow.controller

import io.javalin.Context

object ErrorController {
    fun badRequest(ctx: Context) {
        ctx.result("Bad request")
    }

    fun notFound(ctx: Context) {
        ctx.result("Not found")
    }

    fun unauthorized(ctx: Context) {
        ctx.result("Unauthorized")
    }
}