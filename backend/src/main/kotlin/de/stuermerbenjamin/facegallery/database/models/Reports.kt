package de.stuermerbenjamin.facegallery.database.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Reports : Table("reports") {
    val identifier = integer("identifier").autoIncrement()
    val reporterIdentifier = integer("reporterIdentifier")
    val reportDateTime = timestamp("reportDateTime")

    override val primaryKey = PrimaryKey(Reports.identifier)
}
