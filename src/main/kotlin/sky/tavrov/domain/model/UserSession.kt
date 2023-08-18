package sky.tavrov.domain.model

import io.ktor.server.auth.*

class UserSession(
    val id: String,
    val name: String
): Principal