package de.stuermerbenjamin.facegallery.database.models

import org.jetbrains.exposed.sql.Table

object Faces : Table("faces") {
    val identifier = integer("identifier").autoIncrement()
    val x1 = float("x1").default(0.0f)
    val y1 = float("y1").default(0.0f)
    val x2 = float("x2").default(0.0f)
    val y2 = float("y2").default(0.0f)
    val confidenceScore = float("confidenceScore").default(0.0f)
    val regressionScale = float("regressionScale").default(0.0f)

    // TODO: confirmed by user at datetime
//    val confirmed = bool("confirmed")

    // TODO: reported by user at datetime

    override val primaryKey = PrimaryKey(identifier)
}
