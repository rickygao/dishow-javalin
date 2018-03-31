package xyz.rickygao.dishow

import io.javalin.Context
import io.javalin.Handler
import io.javalin.security.AccessManager
import io.javalin.security.Role
import xyz.rickygao.dishow.RealRole.*

internal enum class RealRole : Role { ANYONE, USER_READ, USER_WRITE }

private val roleMap = mapOf(
        Pair("ricky", "adminpwd") to listOf(USER_READ, USER_WRITE),
        Pair("someone", "somepwd") to listOf(USER_READ)
)

internal val Context.roles: List<Role>
    get() = basicAuthCredentials()?.let { roleMap[Pair(it.username, it.password)] }.orEmpty()

object RealAccessManager : AccessManager {
    override fun manage(handler: Handler, ctx: Context, permittedRoles: List<Role>) {
        when {
            permittedRoles.contains(ANYONE) -> handler.handle(ctx)
            ctx.roles.any { it in permittedRoles } -> handler.handle(ctx)
            else -> ctx.status(401)
        }

    }
}