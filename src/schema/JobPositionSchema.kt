package com.scoville.ats.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.scoville.ats.database.Companies
import com.scoville.ats.database.JobPositions
import com.scoville.ats.models.Company
import com.scoville.ats.models.JobPosition
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class JobPositionQuery {
    @GraphQLDescription("Get a job position")
    fun jobPosition(companyId: UUID, id: UUID): JobPosition? =
        transaction {
            runCatching {
                val row =
                    JobPositions.select { (JobPositions.id eq id) and (JobPositions.companyId eq companyId) }.single()

                JobPosition(
                    id = id,
                    title = row[JobPositions.title],
                    publicId = row[JobPositions.publicId],
                    company = Company(id = companyId)
                )
            }.getOrNull()
        }

    fun jobPositions(companyId: UUID): List<JobPosition> =
        transaction {
            (JobPositions leftJoin Companies).select { JobPositions.companyId eq companyId }.map {
                JobPosition(
                    id = it[JobPositions.id].value,
                    title = it[JobPositions.title],
                    publicId = it[JobPositions.publicId],
                    company = Company(id = companyId)
                )
            }
        }
}

class JobPositionMutation {
    fun createJobPosition(companyId: UUID, title: String): JobPosition =
        transaction {
            val publicId = JobPositions.getNextPublicId(companyId = companyId)

            val id = JobPositions.insert {
                it[this.companyId] = companyId
                it[this.title] = title
                it[this.publicId] = publicId
                it[updatedAt] = DateTime.now()
            } get JobPositions.id

            JobPosition(
                id = id.value,
                title = title,
                publicId = publicId,
                company = Company(id = companyId)
            )
        }
}