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
import sky.tavrov.domain.model.ApiRequest
import sky.tavrov.domain.model.Endpoint
import sky.tavrov.domain.model.UserSession
import sky.tavrov.util.Constants.AUDIENCE
import sky.tavrov.util.Constants.ISSUER

fun Route.tokenVerificationRoute(app: Application) {
    post(Endpoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()

        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId)

            if (result != null) {

                val name = result.payload["name"].toString()
                val email = result.payload["email"].toString()
                app.log.info("TOKEN SUCCESSFULLY VERIFIED: $name $email")


                call.sessions.set(UserSession(id = "123", name = "Oleh"))
                call.respondRedirect(Endpoint.Authorized.path)
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