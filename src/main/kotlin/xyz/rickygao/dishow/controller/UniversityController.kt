package xyz.rickygao.dishow.controller

import io.javalin.Context
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.rickygao.dishow.model.University
import xyz.rickygao.dishow.model.toMap
import xyz.rickygao.dishow.model.toMapWithDetails

internal object UniversityController {

    fun getAllUniversities(ctx: Context) {
        ctx.json(transaction { University.all().map(University::toMap) })
    }

    fun getUniversityById(ctx: Context) {
        ctx.json(ctx.param(":university-id")?.toInt()?.let {
            transaction { University[it].toMapWithDetails() }
        }.orEmpty())
    }

}