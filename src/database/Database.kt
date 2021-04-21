package com.scoville.ats.database

import com.scoville.ats.models.Customer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

val customers = mutableListOf<Customer>()

object Db {
    private val connection by lazy {
        Database.connect(
            "jdbc:postgresql://localhost:5432/kats", user = "postgres", password = "postgres"
        )
    }

    fun init() {
        connection

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Widgets, Companies, Clients, JobPositions)

            if (false) {
                val companyId = Companies.insert {
                    it[updatedAt] = DateTime.now()
                } get Companies.id

                Clients.insert {
                    it[Clients.companyId] = companyId.value
                    it[updatedAt] = DateTime.now()
                }

                JobPositions.insert {
                    it[JobPositions.companyId] = companyId.value
                    it[publicId] = 1
                    it[title] = "First job position"
                    it[updatedAt] = DateTime.now()
                }
            }
        }
    }
}
