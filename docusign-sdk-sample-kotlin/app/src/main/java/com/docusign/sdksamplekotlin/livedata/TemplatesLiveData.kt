package com.docusign.sdksamplekotlin.livedata

import com.docusign.androidsdk.dsmodels.DSTemplate
import com.docusign.androidsdk.dsmodels.DSTemplateDefinition
import com.docusign.androidsdk.dsmodels.DSTemplates
import com.docusign.androidsdk.exceptions.DSTemplateException

data class GetTemplatesModel(val status: Status, val dsTemplates: DSTemplates?, val exception: DSTemplateException?)
data class CacheTemplateModel(val status: Status, val template: DSTemplate?, val position: Int, val exception: DSTemplateException?)
data class RetrieveCachedTemplateModel(
    val status: Status,
    val templateDefinition: DSTemplateDefinition?,
    val position: Int,
    val exception: DSTemplateException?
)
data class RemoveCachedTemplateModel(
    val status: Status,
    val templateDefinition: DSTemplateDefinition?,
    val position: Int,
    val exception: DSTemplateException?
)
data class UseTemplateOfflineModel(val status: Status, val envelopeId: String?, val exception: DSTemplateException?)
data class UseTemplateOnlineModel(val status: Status, val envelopeId: String?, val exception: DSTemplateException?)
