package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.Catalog
import xyz.rickygao.dishow.model.Catalogs
import xyz.rickygao.dishow.model.toMap

internal object CatalogController {

    fun getCatalogById(ctx: Context) {
        ctx.json(ctx.param(":catalog-id")?.toInt()?.let {
            transaction { Catalog[it].toMap() }
        }.orEmpty())
    }

    fun getCatalogsByCanteen(ctx: Context) {
        ctx.json(ctx.param(":canteen-id")?.toInt()?.let {
            transaction { Catalog.find(Catalogs.cid eq it).map(Catalog::toMap) }
        }.orEmpty())
    }

    fun getCatalogsByCanteenAndName(ctx: Context) {
        ctx.json(ctx.param(":canteen-id")?.toInt()?.let { cid ->
            ctx.param(":catalog-name")?.let { name ->
                transaction { Catalog.find((Catalogs.cid eq cid) and (Catalogs.name like "%$name%")).map(Catalog::toMap) }
            }
        }.orEmpty())
    }

}