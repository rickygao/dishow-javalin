package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.*

internal object CatalogController {

    fun getCatalogById(ctx: Context) {
        ctx.param(":catalog-id")?.toInt()?.let {
            transaction { Catalog[it].toMapWithChildren() }
        }.orEmpty().let(ctx::json)
    }

    fun getCatalogsByUniversityAndName(ctx: Context) {
        ctx.param(":university-id")?.toInt()?.let { universityId ->
            ctx.queryParam("name")?.let { name ->
                transaction {
                    (Universities innerJoin Canteens innerJoin Catalogs innerJoin Dishes)
                            .slice(Catalogs.columns)
                            .select((Universities.id eq universityId) and
                                    ((Catalogs.name like "%$name%") or (Dishes.name like "%$name%")))
                            .withDistinct()
                            .let(Catalog.Companion::wrapRows)
                            .map(Catalog::toMapWithParent)
                }
            }
        }.orEmpty().let(ctx::json)
    }

    fun getCatalogsByCanteenAndName(ctx: Context) {
        ctx.param(":canteen-id")?.toInt()?.let { canteenId ->
            ctx.queryParam("name")?.let { name ->
                transaction {
                    (Canteens innerJoin Catalogs innerJoin Dishes)
                            .slice(Catalogs.columns)
                            .select((Canteens.id eq canteenId) and
                                    ((Catalogs.name like "%$name%") or (Dishes.name like "%$name%")))
                            .withDistinct()
                            .let(Catalog.Companion::wrapRows)
                            .map(Catalog::toMapWithParent)
                }
            }
        }.orEmpty().let(ctx::json)
    }

}