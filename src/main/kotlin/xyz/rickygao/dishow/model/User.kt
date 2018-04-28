package xyz.rickygao.dishow.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

internal object Users : IdTable<Int>("users") {
    override val id = integer("id").primaryKey().autoIncrement().entityId()
    val username = varchar("username", 32)
    val password = varchar("password", 32)
}

internal class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var username by Users.username
    var password by Users.password
    val catalogComments by CatalogComment referrersOn CatalogComments.uid
}