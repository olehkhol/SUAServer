package sky.tavrov.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sky.tavrov.domain.model.Endpoint

fun Route.unauthorizedRoute() {
    get(Endpoint.Unautorized.path) {
        call.respond(
            message = "Not Authorized.",
            status = HttpStatusCode.Unauthorized
        )
    }
}