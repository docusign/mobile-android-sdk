package com.docusign.sdksamplekotlin.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.DSTemplatesFilter
import com.docusign.androidsdk.dsmodels.DSTemplates
import com.docusign.androidsdk.dsmodels.DSTemplateDefinition
import com.docusign.androidsdk.dsmodels.DSTemplate
import com.docusign.androidsdk.dsmodels.DSEnvelopeDefaults
import com.docusign.androidsdk.exceptions.DSTemplateException
import com.docusign.androidsdk.listeners.DSTemplateListListener
import com.docusign.androidsdk.listeners.DSTemplateListener
import com.docusign.androidsdk.listeners.DSCacheTemplateListener
import com.docusign.androidsdk.listeners.DSGetCachedTemplateListener
import com.docusign.androidsdk.listeners.DSRemoveTemplateListener
import com.docusign.androidsdk.listeners.DSOnlineUseTemplateListener
import com.docusign.androidsdk.listeners.DSOfflineUseTemplateListener
import com.docusign.sdksamplekotlin.livedata.GetTemplatesModel
import com.docusign.sdksamplekotlin.livedata.CacheTemplateModel
import com.docusign.sdksamplekotlin.livedata.RemoveCachedTemplateModel
import com.docusign.sdksamplekotlin.livedata.RetrieveCachedTemplateModel
import com.docusign.sdksamplekotlin.livedata.UseTemplateOnlineModel
import com.docusign.sdksamplekotlin.livedata.UseTemplateOfflineModel
import com.docusign.sdksamplekotlin.livedata.Status
import com.docusign.sdksamplekotlin.utils.Utils

class TemplatesViewModel : ViewModel() {
    val getTemplatesLiveData: MutableLiveData<GetTemplatesModel> by lazy {
        MutableLiveData<GetTemplatesModel>()
    }

    val cacheTemplateLiveData: MutableLiveData<CacheTemplateModel> by lazy {
        MutableLiveData<CacheTemplateModel>()
    }

    val removeCachedTemplateLiveData: MutableLiveData<RemoveCachedTemplateModel> by lazy {
        MutableLiveData<RemoveCachedTemplateModel>()
    }

    val retrieveCachedTemplateLiveData: MutableLiveData<RetrieveCachedTemplateModel> by lazy {
        MutableLiveData<RetrieveCachedTemplateModel>()
    }

    val useTemplateOnlineLiveData: MutableLiveData<UseTemplateOnlineModel> by lazy {
        MutableLiveData<UseTemplateOnlineModel>()
    }

    val useTemplateOfflineLiveData: MutableLiveData<UseTemplateOfflineModel> by lazy {
        MutableLiveData<UseTemplateOfflineModel>()
    }

    val templateDelegate = DocuSign.getInstance().getTemplateDelegate()

    fun getTemplates(context: Context, filter: DSTemplatesFilter) {
        if (Utils.isNetworkAvailable(context)) {
            // DS: Get templates
            templateDelegate.getTemplates(filter, object : DSTemplateListListener {
                override fun onStart() {
                    val getTemplatesModel = GetTemplatesModel(Status.START, null, null)
                    getTemplatesLiveData.value = getTemplatesModel
                }

                override fun onComplete(dsTemplates: DSTemplates) {
                    val getTemplatesModel = GetTemplatesModel(Status.COMPLETE, dsTemplates, null)
                    getTemplatesLiveData.value = getTemplatesModel
                }

                override fun onError(exception: DSTemplateException) {
                    val getTemplatesModel = GetTemplatesModel(Status.ERROR, null, exception)
                    getTemplatesLiveData.value = getTemplatesModel
                }
            })
        } else {
            // DS: Retrieve downloaded templates
            templateDelegate.retrieveDownloadedTemplates(object : DSTemplateListListener {
                override fun onStart() {
                    val getTemplatesModel = GetTemplatesModel(Status.START, null, null)
                    getTemplatesLiveData.value = getTemplatesModel
                }

                override fun onComplete(dsTemplates: DSTemplates) {
                    val getTemplatesModel = GetTemplatesModel(Status.COMPLETE, dsTemplates, null)
                    getTemplatesLiveData.value = getTemplatesModel
                }

                override fun onError(exception: DSTemplateException) {
                    val getTemplatesModel = GetTemplatesModel(Status.ERROR, null, exception)
                    getTemplatesLiveData.value = getTemplatesModel
                }
            })
        }
    }

    fun cacheTemplate(templateId: String, position: Int) {
        // DS: Get template
        templateDelegate.getTemplate(templateId, null, object : DSTemplateListener {

            override fun onComplete(template: DSTemplateDefinition) {

                if (template.cacheable.isCacheable) {
                    // DS: Cache template
                    templateDelegate.cacheTemplate(template.templateId, object : DSCacheTemplateListener {
                        override fun onStart() {
                            val cacheTemplateModel = CacheTemplateModel(Status.START, null, position, null)
                            cacheTemplateLiveData.value = cacheTemplateModel
                        }

                        override fun onComplete(template: DSTemplate) {
                            val cacheTemplateModel = CacheTemplateModel(Status.COMPLETE, template, position, null)
                            cacheTemplateLiveData.value = cacheTemplateModel
                        }

                        override fun onError(exception: DSTemplateException) {
                            val cacheTemplateModel = CacheTemplateModel(Status.ERROR, null, position, exception)
                            cacheTemplateLiveData.value = cacheTemplateModel
                        }
                    })
                } else {
                    val exception = DSTemplateException("Template not cacheable")
                    val cacheTemplateModel = CacheTemplateModel(Status.ERROR, null, position, exception)
                    cacheTemplateLiveData.value = cacheTemplateModel
                }
            }

            override fun onError(exception: DSTemplateException) {
                val cacheTemplateModel = CacheTemplateModel(Status.ERROR, null, position, exception)
                cacheTemplateLiveData.value = cacheTemplateModel
            }
        })
    }

    fun removeCachedTemplate(templateId: String, position: Int) {
        // DS: Retrieve cached template
        templateDelegate.retrieveCachedTemplate(templateId, object : DSGetCachedTemplateListener {

            override fun onComplete(template: DSTemplateDefinition?) {
                template?.let {
                    // DS: Remove cached template
                    templateDelegate.removeCachedTemplate(template, object : DSRemoveTemplateListener {
                        override fun onTemplateRemoved(isRemoved: Boolean) {
                            if (isRemoved) {
                                val removeCachedTemplateModel = RemoveCachedTemplateModel(Status.COMPLETE, template, position, null)
                                removeCachedTemplateLiveData.value = removeCachedTemplateModel
                            } else {
                                val exception = DSTemplateException("Template not removed")
                                val removeCachedTemplateModel = RemoveCachedTemplateModel(Status.ERROR, null, position, exception)
                                removeCachedTemplateLiveData.value = removeCachedTemplateModel
                            }
                        }
                    })
                }
            }

            override fun onError(exception: DSTemplateException) {
                val removeCachedTemplateModel = RemoveCachedTemplateModel(Status.ERROR, null, position, exception)
                removeCachedTemplateLiveData.value = removeCachedTemplateModel
            }
        })
    }

    fun retrieveCachedTemplate(templateId: String, position: Int) {
        // DS: Retrieve cached template
        templateDelegate.retrieveCachedTemplate(templateId, object : DSGetCachedTemplateListener {
            override fun onComplete(template: DSTemplateDefinition?) {
                if (template != null) {
                    val retrieveCachedTemplateModel = RetrieveCachedTemplateModel(Status.COMPLETE, template, position, null)
                    retrieveCachedTemplateLiveData.value = retrieveCachedTemplateModel
                }
            }

            override fun onError(exception: DSTemplateException) {
                val retrieveCachedTemplateModel = RetrieveCachedTemplateModel(Status.ERROR, null, position, exception)
                retrieveCachedTemplateLiveData.value = retrieveCachedTemplateModel
            }

        })
    }

    fun useTemplateOnline(context: Context, templateId: String, envelopeDefaults: DSEnvelopeDefaults?) {
        // DS: Online signing using template
        templateDelegate.useTemplateOnline(context, templateId, envelopeDefaults, object : DSOnlineUseTemplateListener {

            override fun onStart(envelopeId: String) {
                val useTemplateOnlineModel = UseTemplateOnlineModel(Status.START, envelopeId, null)
                useTemplateOnlineLiveData.value = useTemplateOnlineModel
            }

            override fun onCancel(envelopeId: String, recipientId: String) {
                /* NO-OP */
            }

            override fun onComplete(envelopeId: String, onlySent: Boolean) {
                val useTemplateOnlineModel = UseTemplateOnlineModel(Status.COMPLETE, envelopeId, null)
                useTemplateOnlineLiveData.value = useTemplateOnlineModel
            }

            override fun onError(envelopeId: String?, exception: DSTemplateException) {
                val useTemplateOnlineModel = UseTemplateOnlineModel(Status.ERROR, envelopeId, exception)
                useTemplateOnlineLiveData.value = useTemplateOnlineModel
            }

            override fun onRecipientSigningError(envelopeId: String, recipientId: String, exception: DSTemplateException) {
                /* NO-OP */
            }

            override fun onRecipientSigningSuccess(envelopeId: String, recipientId: String) {
                /* NO-OP */
            }
        })
    }

    fun useTemplateOffline(context: Context, templateId: String, envelopeDefaults: DSEnvelopeDefaults?) {
        // DS: Offline signing using template
        templateDelegate.useTemplateOffline(context, templateId, envelopeDefaults, object : DSOfflineUseTemplateListener {
            override fun onCancel(templateId: String, envelopeId: String?) {
                /* NO-OP */
            }

            override fun onComplete(envelopeId: String) {
                val useTemplateOfflineModel = UseTemplateOfflineModel(Status.COMPLETE, envelopeId, null)
                useTemplateOfflineLiveData.value = useTemplateOfflineModel
            }

            override fun onError(exception: DSTemplateException) {
                val useTemplateOfflineModel = UseTemplateOfflineModel(Status.ERROR, null, exception)
                useTemplateOfflineLiveData.value = useTemplateOfflineModel
            }
        })
    }
}
