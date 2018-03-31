package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.Dish
import xyz.rickygao.dishow.model.Dishes
import xyz.rickygao.dishow.model.toMap

object DishController {

    fun getDishById(ctx: Context) {
        ctx.json(ctx.param(":dish-id")?.toInt()?.let {
            transaction { Dish[it].toMap() }
        }.orEmpty())
    }

    fun getDishesByCatalog(ctx: Context) {
        ctx.json(ctx.param(":catalog-id")?.toInt()?.let {
            transaction { Dish.find(Dishes.cid eq it).map(Dish::toMap) }
        }.orEmpty())
    }

    fun getDishesByCatalogAndName(ctx: Context) {
        ctx.json(ctx.param(":catalog-id")?.toInt()?.let { cid ->
            ctx.param(":dish-name")?.let { name ->
                transaction { Dish.find((Dishes.cid eq cid) and (Dishes.name like "%$name%")).map(Dish::toMap) }
            }
        }.orEmpty())
    }
}