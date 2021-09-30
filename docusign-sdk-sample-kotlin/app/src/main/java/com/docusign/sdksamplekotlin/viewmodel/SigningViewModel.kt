package com.docusign.sdksamplekotlin.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.DSEnvelope
import com.docusign.androidsdk.exceptions.DSSigningException
import com.docusign.androidsdk.listeners.DSCaptiveSigningListener
import com.docusign.androidsdk.listeners.DSOfflineSigningListener
import com.docusign.androidsdk.listeners.DSOnlineSigningListener
import com.docusign.sdksamplekotlin.fragment.ClientInvestmentFragment
import com.docusign.sdksamplekotlin.livedata.*

class SigningViewModel : ViewModel() {

    companion object {
        val TAG = SignOfflineModel::class.java.simpleName
    }

    val signOfflineLiveData: MutableLiveData<SignOfflineModel> by lazy {
        MutableLiveData<SignOfflineModel>()
    }

    val signOnlineLiveData: MutableLiveData<SignOnlineModel> by lazy {
        MutableLiveData<SignOnlineModel>()
    }

    val captiveSigningLiveData: MutableLiveData<CaptiveSigningModel> by lazy {
        MutableLiveData<CaptiveSigningModel>()
    }

    val cachedEnvelopeSigningLiveData: MutableLiveData<CachedEnvelopeSigningModel> by lazy {
        MutableLiveData<CachedEnvelopeSigningModel>()
    }

    private val signingDelegate = DocuSign.getInstance().getSigningDelegate()

    fun signOffline(context: Context, envelopeId: String) {
        // DS: Offline Signing using local envelopeId
        signingDelegate.signOffline(context, envelopeId, object : DSOfflineSigningListener {

            override fun onSuccess(envelopeId: String) {
                val signOfflineModel = SignOfflineModel(Status.COMPLETE, null)
                signOfflineLiveData.value = signOfflineModel
            }

            override fun onCancel(envelopeId: String) {
                /* NO- OP */
            }

            override fun onError(exception: DSSigningException) {
                val signOfflineModel = SignOfflineModel(Status.ERROR, exception)
                signOfflineLiveData.value = signOfflineModel
            }
        })
    }

    fun signOnline(context: Context, envelopeId: String) {
        // DS: Online Signing using local envelopeId
        signingDelegate.createEnvelopeAndLaunchOnlineSigning(context, envelopeId, object : DSOnlineSigningListener {

            override fun onStart(envelopeId: String) {
                val signOnlineModel = SignOnlineModel(Status.START, null)
                signOnlineLiveData.value = signOnlineModel
            }

            override fun onSuccess(envelopeId: String) {
                val signOnlineModel = SignOnlineModel(Status.COMPLETE, null)
                signOnlineLiveData.value = signOnlineModel
            }

            override fun onCancel(envelopeId: String, recipientId: String) {
                /* NO-OP */
            }

            override fun onError(envelopeId: String?, exception: DSSigningException) {
                val signOnlineModel = SignOnlineModel(Status.ERROR, exception)
                signOnlineLiveData.value = signOnlineModel
            }

            override fun onRecipientSigningError(envelopeId: String, recipientId: String, exception: DSSigningException) {
                /* NO-OP */
            }

            override fun onRecipientSigningSuccess(envelopeId: String, recipientId: String) {
                /* NO-OP */
            }
        })
    }

    fun captiveSigning(context: Context, envelope: DSEnvelope) {
        signingDelegate.launchCaptiveSigning(context,
            envelope.envelopeId,
            envelope.recipients?.getOrNull(0)?.clientUserId ?: "",
            object : DSCaptiveSigningListener {
                override fun onCancel(
                    envelopeId: String,
                    recipientId: String
                ) {
                    /* NO-OP */
                }

                override fun onError(
                    envelopeId: String?,
                    exception: DSSigningException
                ) {
                    val captiveSigningModel = CaptiveSigningModel(Status.ERROR, exception)
                    captiveSigningLiveData.value = captiveSigningModel
                }

                override fun onRecipientSigningError(
                    envelopeId: String,
                    recipientId: String,
                    exception: DSSigningException
                ) {
                    /* NO-OP */
                }

                override fun onRecipientSigningSuccess(
                    envelopeId: String,
                    recipientId: String
                ) {
                    /* NO-OP */
                }

                override fun onStart(envelopeId: String) {
                    val captiveSigningModel = CaptiveSigningModel(Status.START, null)
                    captiveSigningLiveData.value = captiveSigningModel
                }

                override fun onSuccess(envelopeId: String) {
                    val captiveSigningModel = CaptiveSigningModel(Status.COMPLETE, null)
                    captiveSigningLiveData.value = captiveSigningModel
                }
            })
    }

    fun signCachedEnvelope(context: Context, envelopeId: String) {
        // DS: Offline Signing using local envelopeId
        signingDelegate.signOffline(context, envelopeId, object : DSOfflineSigningListener {

            override fun onSuccess(envelopeId: String) {
                val cachedEnvelopeSigningModel = CachedEnvelopeSigningModel(Status.COMPLETE, null)
                cachedEnvelopeSigningLiveData.value = cachedEnvelopeSigningModel
            }

            override fun onCancel(envelopeId: String) {
                /* NO- OP */
            }

            override fun onError(exception: DSSigningException) {
                val cachedEnvelopeSigningModel = CachedEnvelopeSigningModel(Status.ERROR, exception)
                cachedEnvelopeSigningLiveData.value = cachedEnvelopeSigningModel
            }
        })
    }
}
