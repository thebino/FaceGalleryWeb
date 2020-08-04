package de.stuermerbenjamin.facegallery.routing

import de.stuermerbenjamin.facegallery.database.DataService
import de.stuermerbenjamin.facegallery.database.dto.Face
import de.stuermerbenjamin.facegallery.detection.Detection
import de.stuermerbenjamin.facegallery.shared.models.Image
import de.stuermerbenjamin.facegallery.shared.response.ImagesResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.tensorflow.TensorFlow
import java.io.File

private const val imagesDir = "../images/"

fun Routing.image(
    dataService: DataService,
    detection: Detection
) {
    authenticate("apikey") {

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

data class ScanResponse(val images: List<Image>)
data class DetectResponse(val images: List<Image>)
