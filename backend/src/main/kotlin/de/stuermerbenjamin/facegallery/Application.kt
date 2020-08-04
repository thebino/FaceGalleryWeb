package de.stuermerbenjamin.facegallery

import com.fasterxml.jackson.databind.SerializationFeature
import de.stuermerbenjamin.facegallery.auth.ApiKeyPrincipal
import de.stuermerbenjamin.facegallery.auth.apiKey
import de.stuermerbenjamin.facegallery.database.DataService
import de.stuermerbenjamin.facegallery.database.DatabaseFactory
import de.stuermerbenjamin.facegallery.database.DatabaseType
import de.stuermerbenjamin.facegallery.detection.Detection
import de.stuermerbenjamin.facegallery.routing.image
import de.stuermerbenjamin.facegallery.routing.index
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.features.CallId
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.callIdMdc
import io.ktor.features.deflate
import io.ktor.features.gzip
import io.ktor.features.minimumSize
import io.ktor.http.HttpHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import nu.pattern.OpenCV
import org.slf4j.event.Level
import java.text.DateFormat
import java.util.UUID

var DEBUG = false
var PRODUCTION = false
private const val VERSION = "0.0.2"

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.main() {
    DatabaseFactory.init(
        databaseType = DatabaseType.SQLITE,
        addSampleData = true
    )

    val dataService = DataService()

    val detection = Detection("src/main/resources/mtcnn_frozen_model.pb")

    val authorizedKeys = hashMapOf(
        "webapp" to "aWxQOHZXQVpmWjFaeTI5Uk1wRUNsTXhsaXd4bTN0SEw=",
        "android" to "MVh4d2RTOGkzWmg4NlFTUTJrblBLcWpqemxnM2lwQ2w="
    )

    mainWithDependencies(dataService, detection, authorizedKeys)
}

fun Application.mainWithDependencies(
    dataService: DataService,
    detection: Detection,
    authorizedApiKeys: HashMap<String, String>
) {
    install(Authentication) {
        apiKey("apikey") {
            validate {
                if (authorizedApiKeys.containsValue(it.value)) {
                    ApiKeyPrincipal(it.value)
                } else {
                    null
                }
            }
        }
    }

    // add a unique ID to response headers (X-Request-ID)
    install(CallId) {
        generate { UUID.randomUUID().toString() }
        verify { it.isNotEmpty() }
        header(HttpHeaders.XRequestId)
    }

    // log client requests to Level.TRACE
    install(CallLogging) {
        level = Level.TRACE
        callIdMdc("request-id")
    }

    // enable compression for outgoing content using gzip & deflate
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024L) // condition
        }
    }

    // check ETag and LastModified to respond with '304 Not Modified'
    install(ConditionalHeaders)

    // de-/serialize requests/responses
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            dateFormat = DateFormat.getDateInstance()
            disableDefaultTyping()
        }
    }

    // add a custom server header with version information
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "facegallery-api/$VERSION")
    }

    install(Routing) {
        // debug routing decicions
        if (DEBUG) {
            trace { application.log.trace(it.buildText()) }
        }

        index()

        image(dataService, detection)
    }
}

fun main(args: Array<String>) {
    OpenCV.loadShared()

    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).apply { start(wait = true) }
}
