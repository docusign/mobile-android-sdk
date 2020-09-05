package com.docusign.sdksamplekotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.DSTemplate
import com.docusign.androidsdk.dsmodels.DSTemplateDefinition
import com.docusign.androidsdk.dsmodels.DSTemplates
import com.docusign.androidsdk.dsmodels.DSTemplatesFilter
import com.docusign.androidsdk.exceptions.DSTemplateException
import com.docusign.androidsdk.listeners.DSTemplateListListener
import com.docusign.androidsdk.listeners.DSTemplateListener
import com.docusign.androidsdk.listeners.DSCacheTemplateListener
import com.docusign.androidsdk.listeners.DSGetCachedTemplateListener
import com.docusign.androidsdk.listeners.DSRemoveTemplateListener
import com.docusign.sdksamplekotlin.livedata.*
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

    val templateDelegate = DocuSign.getInstance().getTemplateDelegate()

    fun getTemplates(filter: DSTemplatesFilter) {
        if (Utils.isNetworkAvailable()) {
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
        templateDelegate.getTemplate(templateId, null, object : DSTemplateListener {

            override fun onComplete(template: DSTemplateDefinition) {

                if (template.cacheable.isCacheable) {
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
        templateDelegate.retrieveCachedTemplate(templateId, object : DSGetCachedTemplateListener {

            override fun onComplete(template: DSTemplateDefinition?) {
                template?.let {
                    templateDelegate.removeCachedTemplate(template, object : DSRemoveTemplateListener {
                        override fun onTemplateRemoved(isRemoved: Boolean) {
                            if (isRemoved) {
                                val removeCachedTemplateModel = RemoveCachedTemplateModel(Status.COMPLETE, template, position,null)
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
}