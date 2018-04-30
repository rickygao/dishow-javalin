package xyz.rickygao.dishow.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

internal object Canteens : IdTable<Int>("canteens") {
    override val id = integer("id").primaryKey().entityId()
    val name = varchar("name", length = 32)
    val location = varchar("location", length = 64).nullable()
    val longitude = decimal("longitude", precision = 10, scale = 7).nullable()
    val latitude = decimal("latitude", precision = 10, scale = 7).nullable()
    val uid = integer("uid").entityId() references Universities.id
}

internal class Canteen(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Canteen>(Canteens)

    var name by Canteens.name
    var location by Canteens.location
    var longitude by Canteens.longitude
    var latitude by Canteens.latitude
    var university by University referencedOn Canteens.uid
    val catalogs by Catalog referrersOn Catalogs.cid
}

internal fun Canteen.toMapWithChildren() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "longitude" to longitude,
        "latitude" to latitude,
        "catalogs" to catalogs.map(Catalog::toMap)
)

internal fun Canteen.toMap() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "longitude" to longitude,
        "latitude" to latitude
)