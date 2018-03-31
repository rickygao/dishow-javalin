package xyz.rickygao.dishow.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

internal object CatalogComments : IdTable<Int>("catalog_comments") {
    override val id = integer("id").primaryKey().autoIncrement().entityId()
    val star = integer("star")
    val detail = varchar("detail", 255).nullable()
    val cid = reference("cid", Catalogs.id)
}

internal class CatalogComment(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CatalogComment>(CatalogComments)

    var star by CatalogComments.star
    var detail by CatalogComments.detail
    var catalog by Catalog referencedOn CatalogComments.cid
}

internal fun CatalogComment.toMap() = mapOf(
        "id" to id.value,
        "star" to star,
        "detail" to detail,
        "cid" to catalog.id.value
)