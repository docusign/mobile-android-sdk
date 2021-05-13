package com.docusign.sdksamplekotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.DSEnvelope
import com.docusign.androidsdk.exceptions.DSEnvelopeException
import com.docusign.androidsdk.exceptions.DSException
import com.docusign.androidsdk.exceptions.DSSyncException
import com.docusign.androidsdk.listeners.DSGetCachedEnvelopeListener
import com.docusign.androidsdk.listeners.DSGetEnvelopeIdsListener
import com.docusign.androidsdk.listeners.DSSyncAllEnvelopesListener
import com.docusign.androidsdk.listeners.DSSyncEnvelopeListener
import com.docusign.sdksamplekotlin.livedata.SyncEnvelopeModel
import com.docusign.sdksamplekotlin.livedata.SyncAllEnvelopesModel
import com.docusign.sdksamplekotlin.livedata.GetSyncPendingEnvelopeIdsModel
import com.docusign.sdksamplekotlin.livedata.GetCachedEnvelopeModel
import com.docusign.sdksamplekotlin.livedata.Status

class EnvelopeViewModel : ViewModel() {

    companion object {
        val TAG = EnvelopeViewModel::class.java.simpleName
    }

    val syncEnvelopeLiveData: MutableLiveData<SyncEnvelopeModel> by lazy {
        MutableLiveData<SyncEnvelopeModel>()
    }

    val syncAllEnvelopesLiveData: MutableLiveData<SyncAllEnvelopesModel> by lazy {
        MutableLiveData<SyncAllEnvelopesModel>()
    }

    val getSyncPendingEnvelopeIdsLiveData: MutableLiveData<GetSyncPendingEnvelopeIdsModel> by lazy {
        MutableLiveData<GetSyncPendingEnvelopeIdsModel>()
    }

    val getCachedEnvelopeLiveData: MutableLiveData<GetCachedEnvelopeModel> by lazy {
        MutableLiveData<GetCachedEnvelopeModel>()
    }

    private val envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate()

    fun syncEnvelope(envelopeId: String, position: Int) {
        // DS: Sync envelope
        envelopeDelegate.syncEnvelope(envelopeId, object : DSSyncEnvelopeListener {
            override fun onError(exception: DSSyncException, localEnvelopeId: String, syncRetryCount: Int?) {
                val syncEnvelopeModel = SyncEnvelopeModel(Status.ERROR, position, exception)
                syncEnvelopeLiveData.value = syncEnvelopeModel
            }

            override fun onSuccess(localEnvelopeId: String, serverEnvelopeId: String?) {
                Log.d(TAG, "Sync envelope Success- Local EnvelopeId: $localEnvelopeId Server EnvelopeId: $serverEnvelopeId")
                val syncEnvelopeModel = SyncEnvelopeModel(Status.COMPLETE, position, null)
                syncEnvelopeLiveData.value = syncEnvelopeModel
            }

        }, true)
    }

    fun syncAllEnvelopes() {
        // DS: Sync all envelopes
        envelopeDelegate.syncAllEnvelopes(object : DSSyncAllEnvelopesListener {

            override fun onStart() {
                val syncAllEnvelopesModel = SyncAllEnvelopesModel(Status.START, null)
                syncAllEnvelopesLiveData.value = syncAllEnvelopesModel
            }

            override fun onComplete(failedEnvelopeIdList: List<String>?) {
                val syncAllEnvelopesModel = SyncAllEnvelopesModel(Status.COMPLETE, null)
                syncAllEnvelopesLiveData.value = syncAllEnvelopesModel
            }

            override fun onEnvelopeSyncError(exception: DSSyncException, localEnvelopeId: String, syncRetryCount: Int?) {
                Log.d(TAG, "Sync Error- Local EnvelopeId: $localEnvelopeId : ${exception.message}")
            }

            override fun onEnvelopeSyncSuccess(localEnvelopeId: String, serverEnvelopeId: String?) {
                /* NO-OP */
            }

            override fun onError(exception: DSException) {
                val syncAllEnvelopesModel = SyncAllEnvelopesModel(Status.ERROR, exception)
                syncAllEnvelopesLiveData.value = syncAllEnvelopesModel
            }

        }, true)
    }

    fun getSyncPendingEnvelopeIds() {
        // DS: Get sync pending envelope Ids
        envelopeDelegate.getSyncPendingEnvelopeIdsList(object : DSGetEnvelopeIdsListener {
            override fun onComplete(envelopeIdList: List<String>) {
                val getSyncPendingEnvelopeIdsModel = GetSyncPendingEnvelopeIdsModel(Status.COMPLETE, envelopeIdList, null)
                getSyncPendingEnvelopeIdsLiveData.value = getSyncPendingEnvelopeIdsModel
            }

            override fun onError(exception: DSEnvelopeException) {
                val getSyncPendingEnvelopeIdsModel = GetSyncPendingEnvelopeIdsModel(Status.ERROR, null, exception)
                getSyncPendingEnvelopeIdsLiveData.value = getSyncPendingEnvelopeIdsModel
            }
        }
        )
    }

    fun getCachedEnvelope(envelopeId: String) {
        // DS: Get cached envelope
        envelopeDelegate.getCachedEnvelope(envelopeId, object : DSGetCachedEnvelopeListener {
            override fun onComplete(envelope: DSEnvelope) {
                val getCachedEnvelopeModel = GetCachedEnvelopeModel(Status.COMPLETE, envelope, null)
                getCachedEnvelopeLiveData.value = getCachedEnvelopeModel
            }

            override fun onError(exception: DSEnvelopeException) {
                val getCachedEnvelopeModel = GetCachedEnvelopeModel(Status.ERROR, null, exception)
                getCachedEnvelopeLiveData.value = getCachedEnvelopeModel
            }

        })
    }
}
