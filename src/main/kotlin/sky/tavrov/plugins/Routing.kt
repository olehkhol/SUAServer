package sky.tavrov.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import sky.tavrov.routes.rootRoute

fun Application.configureRouting() {
    routing {
        rootRoute()
    }
}
