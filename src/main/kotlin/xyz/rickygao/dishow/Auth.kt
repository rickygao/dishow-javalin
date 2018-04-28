package xyz.rickygao.dishow

import io.javalin.Context
import io.javalin.Handler
import io.javalin.security.AccessManager
import io.javalin.security.Role
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.RealRole.ANYONE
import xyz.rickygao.dishow.RealRole.USER
import xyz.rickygao.dishow.model.User
import xyz.rickygao.dishow.model.Users

internal enum class RealRole : Role { ANYONE, USER }

object RealAccessManager : AccessManager {
    override fun manage(handler: Handler, ctx: Context, permittedRoles: List<Role>) {
        when {
            permittedRoles.contains(USER) -> {
                transaction {
                    val username = ctx.header("username").orEmpty()
                    val password = ctx.header("password").orEmpty()
                    User.find((Users.username eq username) and (Users.password eq password)).singleOrNull()
                }?.let {
                    ctx.attribute("id", it.id.value)
                    handler.handle(ctx)
                } ?: ctx.status(401)
            }
            permittedRoles.contains(ANYONE) -> handler.handle(ctx)
            else -> ctx.status(401)
        }
    }
}