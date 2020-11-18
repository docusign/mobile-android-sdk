package com.docusign.sdksamplekotlin.model

data class Appointment(
    val date: String,
    val client: Client,
    val clientSigned: Boolean = false
)
