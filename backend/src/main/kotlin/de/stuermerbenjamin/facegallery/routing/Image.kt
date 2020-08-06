package de.stuermerbenjamin.facegallery.routing

import de.stuermerbenjamin.facegallery.database.DataService
import de.stuermerbenjamin.facegallery.database.dto.Face
import de.stuermerbenjamin.facegallery.detection.Detection
import de.stuermerbenjamin.facegallery.shared.models.Image
import de.stuermerbenjamin.facegallery.shared.response.ImagesResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.tensorflow.TensorFlow
import java.io.File
import java.io.InputStream
import java.io.OutputStream

private const val imagesDir = "../images/"

fun Routing.image(
    dataService: DataService,
    detection: Detection
) {
    authenticate("apikey") {

        // upload an image
        post("/image") {
            val multipart = call.receiveMultipart()
            var name = ""

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> if (part.name == "name") {
                        name = part.value
                        println("Name: $name")
                    }
                    is PartData.FileItem -> {
                        val file = File(File(imagesDir), "${System.currentTimeMillis()}-$name")

                        part.streamProvider().use {inputStream ->
                            file.outputStream().buffered().use {outputStream ->
                                inputStream.copyToSuspend(outputStream)
                            }
                        }
                    }
                    is PartData.BinaryItem -> TODO()
                }
                part.dispose()
            }

            call.respond(HttpStatusCode.Created, "")
        }

        // scan for new files
        get("/scan") {
            val result = arrayListOf<Image>()

            File(imagesDir).walkTopDown().forEach {
                if (it.canonicalPath != File(imagesDir).canonicalPath) {
                    val image = dataService.getImage(it.name)
                    if (image == null) {
                        val newImage = Image(it.name)
                        result.add(newImage)
                        dataService.addImage(newImage)
                    } else {
                        println("skip already known image ${it.name}")
                    }
                }
            }

            call.respond(ScanResponse(result))
        }

        get("/detect") {
            try {
                val result = arrayListOf<Image>()
                val images = dataService.getImages()

                for (image in images) {
                    // TODO: skip if faces for this image are already in database

                    val faces = detection.detect(imagesDir, image)

                    // Add image to response when faces have been detected
                    if (faces.isNotEmpty()) {
                        result.add(image)
                    }

                    for (face in faces) {
                        // TODO: persist detected faces in database
                        dataService.addFace(
                            Face(
                                id = -1,
                                x1 = face.x1,
                                y1 = face.y1,
                                x2 = face.x2,
                                y2 = face.y2,
                                confidenceScore = face.confidenceScore,
                                regressionScale = face.regressionScale
                            )
                        )
                    }
                }

                call.respond(DetectResponse(result))
            } catch (e: Exception) {
                println(e)
                call.respond(HttpStatusCode.InternalServerError, arrayListOf<Image>())
            }
        }

        // get a list of images
        get("/images") {
            call.respond(
                ImagesResponse(
                    TensorFlow.version(),
                    dataService.getImages()
                )
            )
        }
    }
}

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}


data class ScanResponse(val images: List<Image>)
data class DetectResponse(val images: List<Image>)
