package com.docusign.sdksamplekotlin.model

data class Appointment(
        val date: String,
        val client: Client,
        var clientSigned: Boolean = false
)
