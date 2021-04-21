package com.scoville.ats.models

import kotlinx.serialization.Serializable
import java.util.*

data class Widget(val id: UUID, val value: String)

data class Company(val id: UUID)

data class JobPosition(
    val id: UUID,
    val company: Company,
    val title: String,
    val publicId: Int
)

@Serializable
data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
)
