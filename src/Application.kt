package com.scoville.ats

import com.scoville.ats.database.Db
import com.scoville.ats.routes.registerCustomerRoutes
import com.scoville.ats.schema.schema
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import ktor.graphql.Config
import ktor.graphql.graphQL

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    Db.init()

    install(ContentNegotiation) { json() }

    registerCustomerRoutes()

    routing { graphQL("/graphql", schema) { Config(showExplorer = true) } }
}
