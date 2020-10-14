package com.docusign.sdksamplekotlin.model

data class Appointment(
    val date: String,
    val clientName: String,
    val clientSigned: Boolean = false
)
