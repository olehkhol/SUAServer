package sky.tavrov.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import sky.tavrov.domain.model.Endpoint
import sky.tavrov.domain.model.UserSession

fun Application.configureAuth() {
    install(Authentication) {
        session<UserSession>(name = "auth-session") {
            validate { session ->
                session
            }
            challenge {
                call.respondRedirect(Endpoint.Unautorized.path)
            }
        }
    }
}