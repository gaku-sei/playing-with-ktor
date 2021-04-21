package com.scoville.ats.schema

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema

val schema =
    toSchema(
        config = SchemaGeneratorConfig(
            supportedPackages = listOf("com.scoville"),
            hooks = CustomSchemaGeneratorHooks()
        ),
        queries = listOf(
            TopLevelObject(WidgetQuery()),
            TopLevelObject(JobPositionQuery()),
        ),
        mutations = listOf(
            TopLevelObject(WidgetMutation()),
            TopLevelObject(JobPositionMutation())
        )
    )
