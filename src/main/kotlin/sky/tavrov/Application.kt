package sky.tavrov

import io.ktor.server.application.*
import sky.tavrov.plugins.configureMonitoring
import sky.tavrov.plugins.configureRouting
import sky.tavrov.plugins.configureSerialization
import sky.tavrov.plugins.configureSession

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureSession()
}
