package com.docusign.sdksamplekotlin.model

data class Client(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val addressLine1: String,
    val addressLine2: String,
    val addressLine3: String,
    val investmentAmount: String
)
