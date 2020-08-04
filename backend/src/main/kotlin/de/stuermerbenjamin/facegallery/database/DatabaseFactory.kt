package de.stuermerbenjamin.facegallery.database

import de.stuermerbenjamin.facegallery.database.DatabaseFactory.init
import de.stuermerbenjamin.facegallery.database.models.Faces
import de.stuermerbenjamin.facegallery.database.models.Images
import de.stuermerbenjamin.facegallery.database.models.Reports
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Database table creation with optional sample data.
 * @see init function with its parameters for creation details.
 */
object DatabaseFactory {
    /**
     * initialize the database creation
     *
     * @param databaseType set the type of the database (MySQL, SQLite or In-Memory).
     * @param addSampleData enable to add default data during table creation.
     */
    fun init(
        databaseType: DatabaseType = DatabaseType.MEMORY,
        addSampleData: Boolean = false
    ) {
        when (databaseType) {
            DatabaseType.MEMORY -> {
                Database.connect(
                    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                    driver = "org.h2.Driver"
                )
            }
            DatabaseType.MYSQL -> {
                Database.connect(
                    url = "jdbc:mysql://root:root@localhost:3306/facegallery_db?useUnicode=true&serverTimezone=UTC&useSSL=false",
                    driver = "com.mysql.cj.jdbc.Driver"
                )
            }
            DatabaseType.SQLITE -> {
                Database.connect(
                    url = "jdbc:sqlite:facegallery.db",
                    driver = "org.sqlite.JDBC"
                )
            }
        }

        // create tables
        transaction {
            create(Faces)
            create(Images)
            create(Reports)
        }

        // add sample data
        if (addSampleData) {
            transaction {
                Faces.insert {
                    it[x1] = 0.1f
                    it[y1] = 0.1f
                    it[x2] = 0.1f
                    it[y2] = 0.1f
                    it[confidenceScore] = 0.1f
                    it[regressionScale] = 0.1f
                }
            }
        }
    }

    suspend fun <T> dbQuery(
        block: suspend () -> T
    ): T =
        newSuspendedTransaction { block() }
}

enum class DatabaseType {
    MEMORY,
    MYSQL,
    SQLITE
}
