package com.docusign.sdksamplekotlin.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.exceptions.DSException
import com.docusign.androidsdk.exceptions.DSSigningException
import com.docusign.androidsdk.exceptions.DSSyncException
import com.docusign.androidsdk.listeners.DSOfflineSigningListener
import com.docusign.androidsdk.listeners.DSSyncAllEnvelopesListener
import com.docusign.androidsdk.listeners.DSOnlineSigningListener
import com.docusign.sdksamplekotlin.livedata.SignOfflineModel
import com.docusign.sdksamplekotlin.livedata.SignOnlineModel
import com.docusign.sdksamplekotlin.livedata.Status

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

    private val signingDelegate = DocuSign.getInstance().getSigningDelegate()
    private val envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate()

    fun signOffline(context: Context, envelopeId: String) {
        signingDelegate.signOffline(context, envelopeId, object : DSOfflineSigningListener {

            override fun onSuccess(envelopeId: String) {
                envelopeDelegate.syncAllEnvelopes(object : DSSyncAllEnvelopesListener {

                    override fun onStart() {
                        /* NO-OP */
                    }

                    override fun onComplete(failedEnvelopeIdList: List<String>?) {
                        val signOfflineModel = SignOfflineModel(Status.COMPLETE, null)
                        signOfflineLiveData.value = signOfflineModel
                    }

                    override fun onEnvelopeSyncError(exception: DSSyncException, localEnvelopeId: String, syncRetryCount: Int?) {
                         /* NO-OP */
                    }

                    override fun onEnvelopeSyncSuccess(localEnvelopeId: String, serverEnvelopeId: String?) {
                        /* NO-OP */
                    }

                    override fun onError(exception: DSException) {
                        val signingException = DSSigningException(exception.message)
                        val signOfflineModel = SignOfflineModel(Status.ERROR, signingException)
                        signOfflineLiveData.value = signOfflineModel
                    }

                }, true)
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
        signingDelegate.createEnvelopeAndLaunchOnlineSigning(context, envelopeId, object : DSOnlineSigningListener {

            override fun onStart(envelopeId: String) {
                /* NO-OP */
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

}
