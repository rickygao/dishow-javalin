package xyz.rickygao.dishow.model

import org.jetbrains.exposed.dao.IntEntity

fun IntEntity.toIdMap() = mapOf("id" to id.value)