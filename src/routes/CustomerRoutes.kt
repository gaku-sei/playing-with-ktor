package com.scoville.ats.routes

import com.scoville.ats.database.customers
import com.scoville.ats.models.Customer
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.customerRouting() {
    route("/customers") {
        get { call.respond(customers) }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            val customer =
                customers.find { it.id == id } ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(customer)
        }

        post {
            val customer = call.receive<Customer>()

            customers.add(customer)

            call.respond(HttpStatusCode.Created)
        }

        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)


            if (customers.removeIf { it.id == id }) {
                call.respond(HttpStatusCode.Accepted)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

fun Application.registerCustomerRoutes() {
    routing { customerRouting() }
}
