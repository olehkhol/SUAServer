package sky.tavrov.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import sky.tavrov.domain.model.ApiResponse
import sky.tavrov.domain.model.Endpoint
import sky.tavrov.domain.model.UserSession
import sky.tavrov.domain.repository.UserDataSource

fun Route.deleteUserRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        delete(Endpoint.DeleteUser.path) {
            val userSession = call.principal<UserSession>()

            if (userSession == null) {
                app.log.info("INVALID SESSION")

                call.respondRedirect(Endpoint.Unauthorized.path)
            } else {
                try {
                    call.sessions.clear<UserSession>()
                    deleteUserRoute(app, userDataSource, userSession.id)
                } catch (e: Exception) {
                    app.log.info("INVALID SESSION")

                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.deleteUserRoute(
    app: Application,
    userDataSource: UserDataSource,
    userId: String
) {
    val result = userDataSource.deleteUser(userId)

    if (result) {
        app.log.info("USER SUCCESSFULLY DELETED")

        call.respond(
            message = ApiResponse(
                success = true,
                message = "Successfully deleted!"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        app.log.info("ERROR DELETING THE USER")

        call.respond(
            message = ApiResponse(success = false),
            status = HttpStatusCode.BadRequest
        )
    }
}