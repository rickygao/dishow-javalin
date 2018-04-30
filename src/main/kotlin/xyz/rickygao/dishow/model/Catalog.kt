package xyz.rickygao.dishow.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

internal object Catalogs : IdTable<Int>("catalogs") {
    override val id = integer("id").primaryKey().entityId()
    val name = varchar("name", length = 32)
    val location = varchar("location", length = 16).nullable()
    val cid = reference("cid", Canteens.id)
}

internal class Catalog(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Catalog>(Catalogs)

    var name by Catalogs.name
    var location by Catalogs.location
    var canteen by Canteen referencedOn Catalogs.cid
    val comments by CatalogComment referrersOn CatalogComments.cid
    val dishes by Dish referrersOn Dishes.cid
    val avgStar get() = comments.asSequence().map(CatalogComment::star).average().takeIf(Double::isFinite)
}

internal fun Catalog.toMapWithChildren() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "avg_star" to avgStar,
        "dishes" to dishes.map(Dish::toMap)
)

internal fun Catalog.toMap() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "avg_star" to avgStar
)

internal fun Catalog.toMapOnlyComments() = mapOf(
        "avg_star" to avgStar,
        "comments" to comments.map(CatalogComment::toMap)
)

internal fun Catalog.toMapWithParent() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "avg_star" to avgStar,
        "canteen" to canteen.toMap()
)