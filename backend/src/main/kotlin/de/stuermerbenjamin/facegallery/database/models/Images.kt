package de.stuermerbenjamin.facegallery.database.models

import org.jetbrains.exposed.sql.Table

object Images : Table("images") {
    val identifier = integer("identifier").autoIncrement()
    val imagePath = varchar("imagePath", Int.MAX_VALUE)
    // location
    // date
    // faces

    override val primaryKey = PrimaryKey(identifier)
}
