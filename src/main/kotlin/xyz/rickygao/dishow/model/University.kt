package xyz.rickygao.dishow.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass


internal object Universities : IdTable<Int>("universities") {
    override val id = integer("id").primaryKey().entityId()
    val name = varchar("name", length = 32)
    val location = varchar("location", length = 64).nullable()
    val longitude = decimal("longitude", precision = 10, scale = 7).nullable()
    val latitude = decimal("latitude", precision = 10, scale = 7).nullable()
}

internal class University(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<University>(Universities)

    var name by Universities.name
    var location by Universities.location
    var longitude by Universities.longitude
    var latitude by Universities.latitude
    val canteens by Canteen referrersOn Canteens.uid
}

internal fun University.toMapWithDetails() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "longitude" to longitude,
        "latitude" to latitude,
        "canteens" to canteens.map(Canteen::toMap)
)

internal fun University.toMap() = mapOf(
        "id" to id.value,
        "name" to name,
        "location" to location,
        "longitude" to longitude,
        "latitude" to latitude
)