package sky.tavrov.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import sky.tavrov.domain.model.ApiResponse
import sky.tavrov.domain.model.Endpoint
import sky.tavrov.domain.model.UserSession
import sky.tavrov.domain.model.UserUpdate
import sky.tavrov.domain.repository.UserDataSource

fun Route.updateUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        put(Endpoint.UpdateUser.path) {
            val userSession = call.principal<UserSession>()
            val userUpdate = call.receive<UserUpdate>()

            if (userSession == null) {
                app.log.info("INVALID SESSION")

                call.respondRedirect(Endpoint.Unauthorized.path)
            } else {
                try {
                    updateUserInfoRoute(app, userDataSource, userSession.id, userUpdate)
                } catch (e: Exception) {
                    app.log.info("INVALID SESSION")

                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.updateUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource,
    userId: String,
    userUpdate: UserUpdate
) {
    val (firstName, lastName) = userUpdate
    val response = userDataSource.updateUser(userId, firstName, lastName)

    if (response) {
        app.log.info("USER SUCCESSFULLY UPDATED")

        call.respond(
            message = ApiResponse(
                success = true,
                message = "Successfully updated!"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        app.log.info("ERROR UPDATING THE USER")

        call.respond(
            message = ApiResponse(success = false),
            status = HttpStatusCode.BadRequest
        )
    }
}