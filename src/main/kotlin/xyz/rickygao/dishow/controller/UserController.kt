package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.User
import xyz.rickygao.dishow.model.Users
import xyz.rickygao.dishow.model.toIdMap

internal object UserController {

    fun getUserByUsernameAndPassword(ctx: Context) {
        ctx.param(":username")?.let { username ->
            ctx.param(":password")?.let { password ->
                transaction {
                    User.find((Users.username eq username) and (Users.password eq password))
                            .singleOrNull()?.toIdMap()
                }
            }
        }.orEmpty().let(ctx::json)
    }

    fun putUserByUsernameAndPassword(ctx: Context) {
        ctx.param(":username")?.let { username ->
            ctx.param(":password")?.let { password ->
                transaction {
                    User.new {
                        this.username = username
                        this.password = password
                    }.toIdMap()
                }
            }
        }.orEmpty().let(ctx::json)
    }

}