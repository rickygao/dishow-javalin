package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.bodyAsClass
import xyz.rickygao.dishow.model.*

object CatalogCommentController {

    fun getCatalogCommentById(ctx: Context) {
        ctx.param("catalog-comment-id")?.toInt()?.let { ccid ->
            transaction { CatalogComment[ccid].toMap() }
        }.orEmpty().let(ctx::json)
    }

    fun getCatalogCommentsByCatalog(ctx: Context) {
        ctx.param("catalog-id")?.toInt()?.let { cid ->
            transaction { Catalog[cid].toMapOnlyComments() }
        }.orEmpty().let(ctx::json)
    }

    private data class CatalogCommentBody(val star: Int, val detail: String? = null, val anonymous: Boolean)

    fun postCatalogComment(ctx: Context) {
        ctx.param("catalog-id")?.toInt()?.let { cid ->
            ctx.bodyAsClass<CatalogCommentBody>().let { body ->
                transaction {
                    CatalogComment.new {
                        this.star = body.star
                        this.detail = body.detail
                        this.catalog = Catalog[cid]
                        this.user = User[ctx.attribute<Int>("id")]
                        this.anonymous = body.anonymous
                    }.toIdMap()
                }
            }
        }.orEmpty().let(ctx::json)
    }
}