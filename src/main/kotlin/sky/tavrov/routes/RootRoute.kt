package sky.tavrov.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sky.tavrov.domain.model.Endpoint

fun Routing.rootRoute() {
    get(Endpoint.Root.path) {
        call.respondText("Welcome to Ktor Server!")
    }
}