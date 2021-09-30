package com.docusign.sdksamplekotlin.model

data class Client(
    var id: String,
    var name: String,
    var phone: String,
    var email: String,
    var addressLine1: String,
    var addressLine2: String,
    var addressLine3: String,
    var investmentAmount: String,
    var storePref: String,
    var cacheEnvelope: Boolean = false
)
