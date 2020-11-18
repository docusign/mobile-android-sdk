package com.docusign.sdksamplekotlin.model

import java.io.File

data class AccreditedInvestorVerification(
    val clientName: String,
    val clientAddress: String,
    val verifier: AccreditedInvestorVerifier,
    val file: File
)

data class AccreditedInvestorVerifier(
    val name: String,
    val company: String,
    val licenseNumber: String,
    val stateRegistered: String,
    val addressLine1: String,
    val addressLine2: String?,
    val addressLine3: String
)
