package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.Dish
import xyz.rickygao.dishow.model.toMap

object DishController {

    fun getDishById(ctx: Context) {
        ctx.json(ctx.param(":dish-id")?.toInt()?.let {
            transaction { Dish[it].toMap() }
        }.orEmpty())
    }

}