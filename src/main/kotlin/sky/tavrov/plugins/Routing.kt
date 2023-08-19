package sky.tavrov.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import sky.tavrov.routes.authorizedRoute
import sky.tavrov.routes.rootRoute
import sky.tavrov.routes.tokenVerificationRoute
import sky.tavrov.routes.unauthorizedRoute

fun Application.configureRouting() {
    routing {
        rootRoute()
        tokenVerificationRoute(application)
        authorizedRoute()
        unauthorizedRoute()
    }
}
