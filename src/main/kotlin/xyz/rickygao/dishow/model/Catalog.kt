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
}

internal fun Catalog.toMap() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "cid" to canteen.id.value
)