package xyz.rickygao.dishow

import io.javalin.Context

internal inline fun <reified T> Context.bodyAsClass() = bodyAsClass(T::class.java)