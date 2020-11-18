package com.docusign.sdksamplekotlin.livedata

import com.docusign.androidsdk.dsmodels.DSEnvelope
import com.docusign.androidsdk.exceptions.DSEnvelopeException
import com.docusign.androidsdk.exceptions.DSException
import com.docusign.androidsdk.exceptions.DSSyncException

data class SyncEnvelopeModel(
    val status: Status,
    val position: Int,
    val exception: DSSyncException?
)

data class SyncAllEnvelopesModel(
    val status: Status,
    val exception: DSException?
)

data class GetSyncPendingEnvelopeIdsModel(
    val status: Status,
    val envelopeIds: List<String>?,
    val exception: DSEnvelopeException?
)

data class GetCachedEnvelopeModel(
    val status: Status,
    val envelope: DSEnvelope?,
    val exception: DSEnvelopeException?
)
