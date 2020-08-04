package de.stuermerbenjamin.facegallery.database

import de.stuermerbenjamin.facegallery.KtorTest
import de.stuermerbenjamin.facegallery.database.dto.Face
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataServiceTest : KtorTest() {
    private val dataService = DataService()

    @Test
    fun testAddFace() = runBlocking {
        // given
        val face = createDefaultFace()

        // when
        val saved = dataService.addFace(face)

        // then
        val retrieved = dataService.getFaces()
        assertThat(retrieved.size).isEqualTo(1)
        assertThat(retrieved[0]).isEqualTo(saved)
        assertThat(retrieved[0].id).isEqualTo(face.id)
        assertThat(retrieved[0].x1).isEqualTo(face.x1)
        assertThat(retrieved[0].y1).isEqualTo(face.y1)
        assertThat(retrieved[0].x2).isEqualTo(face.x2)
        assertThat(retrieved[0].y2).isEqualTo(face.y2)
        assertThat(retrieved[0].confidenceScore).isEqualTo(face.confidenceScore)
        assertThat(retrieved[0].regressionScale).isEqualTo(face.regressionScale)

        Unit
    }

    private fun createDefaultFace(): Face {
        return Face(
            id = 1337,
            x1 = 1.1f,
            y1 = 1.2f,
            x2 = 5.5f,
            y2 = 6.6f,
            confidenceScore = 0.998f,
            regressionScale = 0.1f
        )
    }
}
