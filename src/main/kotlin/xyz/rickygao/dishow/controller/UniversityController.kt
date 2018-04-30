package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.University
import xyz.rickygao.dishow.model.toMap
import xyz.rickygao.dishow.model.toMapWithChildren

internal object UniversityController {

    fun getAllUniversities(ctx: Context) {
        transaction { University.all().map(University::toMap) }.let(ctx::json)
    }

    fun getUniversityById(ctx: Context) {
        ctx.param(":university-id")?.toInt()?.let {
            transaction { University[it].toMapWithChildren() }
        }.orEmpty().let(ctx::json)
    }

}