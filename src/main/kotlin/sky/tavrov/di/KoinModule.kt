package sky.tavrov.di

import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import sky.tavrov.util.Constants.DATABASE_NAME

val koinModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase(DATABASE_NAME)
    }
}