package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.bodyAsClass
import xyz.rickygao.dishow.model.Catalog
import xyz.rickygao.dishow.model.CatalogComment
import xyz.rickygao.dishow.model.CatalogComments
import xyz.rickygao.dishow.model.toMap

object CatalogCommentController {

    fun getCatalogCommentById(ctx: Context) {
        ctx.json(ctx.param("catalog-comment-id")?.toInt()?.let { ccid ->
            transaction { CatalogComment[ccid].toMap() }
        }.orEmpty())
    }

    fun getCatalogCommentsByCatalog(ctx: Context) {
        ctx.json(ctx.param("catalog-id")?.toInt()?.let { cid ->
            transaction { CatalogComment.find(CatalogComments.cid eq cid).map(CatalogComment::toMap) }
        }.orEmpty().let {
            mapOf("avg_star" to if (it.isEmpty()) 0.0 else it.sumBy { it["star"] as Int }.toDouble() / it.size,
                    "comments" to it)
        })
    }

    private data class CatalogCommentBody(val star: Int, val detail: String?)

    fun postCatalogComment(ctx: Context) {
        ctx.json(ctx.param("catalog-id")?.toInt()?.let { cid ->
            ctx.param("star")?.toInt()?.let { star ->
                ctx.bodyAsClass<CatalogCommentBody>().let { body ->
                    mapOf("id" to transaction {
                        CatalogComment.new {
                            this.star = body.star
                            this.detail = body.detail
                            this.catalog = Catalog[cid]
                        }.id.value
                    })
                }
            }
        }.orEmpty())
    }

}