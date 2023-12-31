package sky.tavrov.routes

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import sky.tavrov.domain.model.ApiRequest
import sky.tavrov.domain.model.Endpoint
import sky.tavrov.domain.model.User
import sky.tavrov.domain.model.UserSession
import sky.tavrov.domain.repository.UserDataSource
import sky.tavrov.util.Constants.AUDIENCE
import sky.tavrov.util.Constants.ISSUER

fun Route.tokenVerificationRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    post(Endpoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()

        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId)

            if (result != null) {
                saveUserToDatabase(app, result, userDataSource)
            } else {
                app.log.info("TOKEN VERIFICATION FAILED")

                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        } else {
            app.log.info("EMPTY TOKEN ID")

            call.respondRedirect(Endpoint.Unauthorized.path)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDatabase(
    app: Application,
    idToken: GoogleIdToken,
    userDataSource: UserDataSource
) {
    val sub = idToken.payload["sub"].toString()
    val name = idToken.payload["name"].toString()
    val (firstName, lastName) = name.split(" ", limit = 2)
    val email = idToken.payload["email"].toString()
    val picture = idToken.payload["picture"].toString()
    val user = User(sub, firstName, lastName, email, picture)
    val result = userDataSource.saveUserInfo(user)

    if (result) {
        app.log.info("USER SUCCESSFULLY SAVED/RETRIEVED")

        call.sessions.set(UserSession(id = sub, name = name))
        call.respondRedirect(Endpoint.Authorized.path)
    } else {
        app.log.info("ERROR SAVING THE USER")

        call.respondRedirect(Endpoint.Unauthorized.path)
    }
}

fun verifyGoogleTokenId(tokenId: String): GoogleIdToken? =
    try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()

        verifier.verify(tokenId)
    } catch (e: Exception) {
        null
    }