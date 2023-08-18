package sky.tavrov.domain.model

sealed class Endpoint(val path: String) {
    object Root: Endpoint(path = "/")
    object TokenVerification: Endpoint(path = "/token_verification")
    object GetUserInfo: Endpoint(path = "/get_user")
    object UpdateUser: Endpoint(path = "/update_user")
    object DeleteUser: Endpoint(path = "/delete_user")
    object SignOut: Endpoint(path = "/sign_out")
    object Unautorized: Endpoint(path = "/unautorized")
    object Autorized: Endpoint(path = "/autorized")
}