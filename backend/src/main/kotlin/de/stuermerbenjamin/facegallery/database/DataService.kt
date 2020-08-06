package de.stuermerbenjamin.facegallery.database

import de.stuermerbenjamin.facegallery.database.DatabaseFactory.dbQuery
import de.stuermerbenjamin.facegallery.database.dto.Face
import de.stuermerbenjamin.facegallery.database.models.Faces
import de.stuermerbenjamin.facegallery.database.models.Images
import de.stuermerbenjamin.facegallery.shared.models.Image
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class DataService {
    suspend fun getImages(): List<Image> = dbQuery {
        val images = Images.selectAll().orderBy(Images.identifier)

        return@dbQuery images.mapNotNull {
            Image(it[Images.imagePath])
        }
    }

    suspend fun getImage(identifier: Int): Image? = dbQuery {
        val row = Images.select { Images.identifier eq identifier }.singleOrNull()

        row?.let {
            return@dbQuery Image(
                imagePath = row[Images.imagePath]
            )
        }

        return@dbQuery null
    }

    suspend fun getImage(imagePath: String): Image? = dbQuery {
        val row = Images.select { Images.imagePath eq imagePath }.singleOrNull()

        row?.let {
            return@dbQuery Image(
                imagePath = row[Images.imagePath]
            )
        }

        return@dbQuery null
    }

    suspend fun addImage(image: Image): Image {
        var key = -1
        dbQuery {
            key = (Images.insert {
                it[this.imagePath] = image.imagePath
            } get Images.identifier)
        }

        return getImage(key)!!
    }

    suspend fun getFaces(): List<Face> = dbQuery {
        val faces = Faces.selectAll()

        return@dbQuery faces.map {
            getFace(it[Faces.identifier])
        }.filterNotNull()
    }

    suspend fun getFace(id: Int): Face? = dbQuery {
        val row = Faces.select { Faces.identifier eq id }.singleOrNull()

        row?.let {
            return@dbQuery Face(
                id = row[Faces.identifier],
                x1 = row[Faces.x1],
                y1 = row[Faces.y1],
                x2 = row[Faces.x2],
                y2 = row[Faces.y2],
                confidenceScore = row[Faces.confidenceScore],
                regressionScale = floatArrayOf() //row[Faces.regressionScale]
            )
        }

        return@dbQuery null
    }

    suspend fun addFace(face: Face): Face {
        var key = -1
        dbQuery {
            key = (Faces.insert {
                it[this.identifier] = face.id
                it[x1] = face.x1
                it[y1] = face.y1
                it[x2] = face.x2
                it[y2] = face.y2
                it[confidenceScore] = face.confidenceScore
//                it[regressionScale] = face.regressionScale
            } get Faces.identifier)
        }

        return getFace(key)!!
    }
}
