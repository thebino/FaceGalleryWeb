package de.stuermerbenjamin.facegallery.frontend

import com.google.gson.Gson
import de.stuermerbenjamin.facegallery.frontend.routing.index
import de.stuermerbenjamin.facegallery.frontend.routing.login
import de.stuermerbenjamin.facegallery.frontend.services.ImageService
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.form
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.features.UserAgent
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.features.AutoHeadResponse
import io.ktor.features.CallId
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ConditionalHeaders
import io.ktor.features.StatusPages
import io.ktor.features.callIdMdc
import io.ktor.features.deflate
import io.ktor.features.gzip
import io.ktor.features.minimumSize
import io.ktor.features.statusFile
import io.ktor.freemarker.FreeMarker
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.directorySessionStorage
import io.ktor.sessions.SessionSerializer
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.util.KtorExperimentalAPI
import org.slf4j.event.Level
import java.io.File
import java.lang.reflect.Type
import java.util.UUID

var DEBUG = false

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.main() {
    val httpClient = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        install(UserAgent) {
            agent = "CGW Webapp"
        }

        engine {
            maxConnectionsCount = 1000
            endpoint {
                /**
                 * Maximum number of requests for a specific endpoint route.
                 */
                maxConnectionsPerRoute = 100

                /**
                 * Max size of scheduled requests per connection(pipeline queue size).
                 */
                pipelineMaxSize = 20

                /**
                 * Max number of milliseconds to keep iddle connection alive.
                 */
                keepAliveTime = 5000

                /**
                 * Number of milliseconds to wait trying to connect to the server.
                 */
                connectTimeout = 5000

                /**
                 * Maximum number of attempts for retrying a connection.
                 */
                connectRetryAttempts = 5
            }
        }
    }
    val imageService = ImageService(httpClient)

    mainWithDependencies(imageService)
}

fun Application.mainWithDependencies(imageService: ImageService) {
    install(Authentication) {
        form {
            userParamName = "username"
            passwordParamName = "password"
            challenge { call.respondRedirect("/") }
            validate {
                if (it.name == "test" && it.password == "test") {
                    UserIdPrincipal(it.name)
                } else {
                    null
                }
            }
            skipWhen {
                val session = it.sessions.get<WebAppSession>()

                session != null
            }
        }
    }

    install(AutoHeadResponse)

    install(CallId) {
        generate { UUID.randomUUID().toString() }
        verify { it.isNotEmpty() }
        header(HttpHeaders.XRequestId)
    }

    install(CallLogging) {
        level = Level.DEBUG
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

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Routing) {
        // debug routing decicions
        if (DEBUG) {
            trace { application.log.trace(it.buildText()) }
        }

        index(imageService)
        login()
    }

    install(StatusPages) {
        statusFile(
            HttpStatusCode.NotFound,
            HttpStatusCode.Unauthorized,
            HttpStatusCode.InternalServerError,
            filePattern = "error/error#.html"
        )
    }

    install(Sessions) {
        cookie<WebAppSession>(
            "SESSION",
            storage = directorySessionStorage(File(".sessions"), cached = true)) {
            serializer = FaceSessionSerializer(WebAppSession::class.java)
            cookie.path = "/"
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).apply { start(wait = true) }
}

data class WebAppSession(val name: String)

class FaceSessionSerializer(
    val type: Type,
    val gson: Gson = Gson(),
    configure: Gson.() -> Unit = {}
) : SessionSerializer<WebAppSession> {
    init {
        configure(gson)
    }

    override fun serialize(session: WebAppSession): String = gson.toJson(session)
    override fun deserialize(text: String): WebAppSession = gson.fromJson(text, type)
}
