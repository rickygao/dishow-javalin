package xyz.rickygao.dishow.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass


internal object Dishes : IdTable<Int>("dishes") {
    override val id = integer("id").primaryKey().entityId()
    val name = varchar("name", length = 32)
    val price = decimal("price", precision = 6, scale = 2).nullable()
    val cid = reference("cid", Catalogs.id)
}

internal class Dish(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Dish>(Dishes)

    var name by Dishes.name
    var price by Dishes.price
    var catalog by Catalog referencedOn Dishes.cid
}

internal fun Dish.toMap() = mapOf(
        "id" to id.value,
        "name" to name,
        "price" to price
)