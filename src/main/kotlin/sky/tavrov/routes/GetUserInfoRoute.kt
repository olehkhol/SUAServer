package sky.tavrov.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sky.tavrov.domain.model.ApiResponse
import sky.tavrov.domain.model.Endpoint
import sky.tavrov.domain.model.UserSession
import sky.tavrov.domain.repository.UserDataSource

fun Route.getUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        get(Endpoint.GetUserInfo.path) {
            val userSession = call.principal<UserSession>()

            if (userSession == null) {
                app.log.info("INVALID SESSION")

                call.respondRedirect(url = Endpoint.Unauthorized.path)
            } else {
                try {
                    call.respond(
                        message = ApiResponse(
                            success = true,
                            user = userDataSource.getUserInfo(userSession.id)
                        )
                    )
                } catch (e: Exception) {
                    app.log.info("GETTING USER INFO ERROR")

                    call.respondRedirect(url = Endpoint.Unauthorized.path)
                }
            }
        }
    }
}