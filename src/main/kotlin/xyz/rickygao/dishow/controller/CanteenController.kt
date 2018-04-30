package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.Canteen
import xyz.rickygao.dishow.model.toMapWithChildren

internal object CanteenController {

    fun getCanteenById(ctx: Context) {
        ctx.param(":canteen-id")?.toInt()?.let {
            transaction { Canteen[it].toMapWithChildren() }
        }.orEmpty().let(ctx::json)
    }

}