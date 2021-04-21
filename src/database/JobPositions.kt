package com.scoville.ats.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object JobPositions : UUIDTable(name = "job_positions") {
    val companyId: Column<UUID> = uuid("company_id").references(Companies.id).index()
    val title: Column<String> = varchar("title", length = 255)
    val publicId: Column<Int> = integer("public_id")
    val createdAt: Column<DateTime> = datetime("created_at").default(DateTime.now())
    val updatedAt: Column<DateTime> = datetime("updated_at")

    init {
        index(isUnique = true, companyId, title)
        index(isUnique = true, companyId, publicId)
    }

    fun getNextPublicId(companyId: UUID): Int =
        transaction {
            val lastIndex =
                JobPositions.slice(JobPositions.publicId.max()).select { JobPositions.companyId eq companyId }
                    .single()[JobPositions.publicId.max()]

            (lastIndex ?: 0) + 1
        }
}
