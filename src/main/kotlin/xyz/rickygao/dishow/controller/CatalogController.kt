package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.Catalog
import xyz.rickygao.dishow.model.toMapWithDetails

internal object CatalogController {

    fun getCatalogById(ctx: Context) {
        ctx.json(ctx.param(":catalog-id")?.toInt()?.let {
            transaction { Catalog[it].toMapWithDetails() }
        }.orEmpty())
    }

}