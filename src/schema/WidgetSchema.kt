package com.scoville.ats.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.scoville.ats.database.Widgets
import com.scoville.ats.models.Widget
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class WidgetQuery {
    @GraphQLDescription("Get a widget")
    fun widget(id: UUID): Widget? =
        transaction {
            runCatching {
                val row = Widgets.select { Widgets.id eq id }.single()

                Widget(id = id, value = row[Widgets.value])
            }
                .getOrNull()
        }
}

class WidgetMutation {
    fun createWidget(value: String): Widget =
        transaction {
            val id = Widgets.insert {
                it[this.value] = value
                it[updatedAt] = DateTime.now()
            } get Widgets.id

            Widget(id = id.value, value = value)
        }
}
