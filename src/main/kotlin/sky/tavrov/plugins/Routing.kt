package sky.tavrov.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import sky.tavrov.domain.repository.UserDataSource
import sky.tavrov.routes.authorizedRoute
import sky.tavrov.routes.rootRoute
import sky.tavrov.routes.tokenVerificationRoute
import sky.tavrov.routes.unauthorizedRoute

fun Application.configureRouting() {
    routing {
        val userDataSource: UserDataSource by inject(UserDataSource::class.java)

        rootRoute()
        tokenVerificationRoute(application, userDataSource)
        authorizedRoute()
        unauthorizedRoute()
    }
}
