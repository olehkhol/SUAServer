package sky.tavrov.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import sky.tavrov.di.koinModule

fun Application.configureKoin() {
    install(Koin) {
        modules(koinModule)
    }
}