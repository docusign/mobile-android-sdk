package com.docusign.sdksamplekotlin.model

data class Client(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val address: String
)