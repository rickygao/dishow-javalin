package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.Canteen
import xyz.rickygao.dishow.model.Canteens
import xyz.rickygao.dishow.model.toMap

internal object CanteenController {

    fun getCanteenById(ctx: Context) {
        ctx.json(ctx.param(":canteen-id")?.toInt()?.let {
            transaction { Canteen[it].toMap() }
        }.orEmpty())
    }

    fun getCanteensByUniversity(ctx: Context) {
        ctx.json(ctx.param(":university-id")?.toInt()?.let {
            transaction { Canteen.find(Canteens.uid eq it).map(Canteen::toMap) }
        }.orEmpty())
    }

    fun getCanteensByUniversityAndName(ctx: Context) {
        ctx.json(ctx.param(":university-id")?.toInt()?.let { uid ->
            ctx.param(":canteen-name")?.let { name ->
                transaction { Canteen.find((Canteens.uid eq uid) and (Canteens.name like "%$name%")).map(Canteen::toMap) }
            }
        }.orEmpty())
    }

}