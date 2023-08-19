package sky.tavrov.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sky.tavrov.domain.model.ApiResponse
import sky.tavrov.domain.model.Endpoint

fun Route.authorizedRoute() {
    authenticate("auth-session") {
        get(Endpoint.Authorized.path) {
            call.respond(
                message = ApiResponse(success = true),
                status = HttpStatusCode.OK
            )
        }
    }
}