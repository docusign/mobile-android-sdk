package com.docusign.sdksamplekotlin.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.adapter.PendingSyncAdapter
import com.docusign.sdksamplekotlin.livedata.GetSyncPendingEnvelopeIdsModel
import com.docusign.sdksamplekotlin.livedata.GetCachedEnvelopeModel
import com.docusign.sdksamplekotlin.livedata.SyncEnvelopeModel
import com.docusign.sdksamplekotlin.livedata.SyncAllEnvelopesModel
import com.docusign.sdksamplekotlin.livedata.Status
import com.docusign.sdksamplekotlin.viewmodel.EnvelopeViewModel

class PendingSyncFragment : Fragment() {

    private lateinit var envelopeViewModel: EnvelopeViewModel
    private lateinit var clientsRecyclerView: RecyclerView
    private var pendingSyncAdapter: PendingSyncAdapter? = null

    companion object {
        val TAG = PendingSyncFragment::class.java.simpleName

        fun newInstance(): PendingSyncFragment {
            val bundle = Bundle().apply {
            }
            val fragment = PendingSyncFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pending_sync, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        envelopeViewModel = EnvelopeViewModel()
        initLiveDataObservers()

        activity?.apply {
            val syncAllButton = findViewById<Button>(R.id.sync_all_button)
            syncAllButton.setOnClickListener {
                toggleProgressBar(true)
                envelopeViewModel.syncAllEnvelopes()
            }
            val synPendingErrorTextView = findViewById<TextView>(R.id.pending_sync_error_text_view)
            synPendingErrorTextView.visibility = View.GONE
            syncAllButton.visibility = View.VISIBLE
            clientsRecyclerView = findViewById(R.id.pending_sync_recycler_view)
            clientsRecyclerView.layoutManager = LinearLayoutManager(this)
            pendingSyncAdapter = PendingSyncAdapter(object : PendingSyncAdapter.PendingSyncListener {

                override fun syncEnvelope(position: Int, envelopeId: String) {
                    toggleProgressBar(true)
                    envelopeViewModel.syncEnvelope(envelopeId, position)
                }
            })
            clientsRecyclerView.adapter = pendingSyncAdapter
        }
        envelopeViewModel.getSyncPendingEnvelopeIds()
    }

    private fun initLiveDataObservers() {
        envelopeViewModel.getSyncPendingEnvelopeIdsLiveData.observe(viewLifecycleOwner, Observer<GetSyncPendingEnvelopeIdsModel> { model ->
            when (model.status) {
                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    if (model.envelopeIds.isNullOrEmpty()) {
                        displayError(resources.getString(R.string.syn_pending_error))
                        return@Observer
                    }
                    model.envelopeIds.forEach { envelopeId ->
                        envelopeViewModel.getCachedEnvelope(envelopeId)
                    }
                }

                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TemplatesFragment.TAG, exception.message!!)
                        displayError(exception.message!!)
                    }
                }
                else -> {
                    /* NO-OP */
                }
            }
        })
        envelopeViewModel.getCachedEnvelopeLiveData.observe(viewLifecycleOwner, Observer<GetCachedEnvelopeModel> { model ->
            when (model.status) {
                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    model.envelope?.let { envelope ->
                        pendingSyncAdapter?.addItem(envelope)
                    }
                }

                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TemplatesFragment.TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    /* NO-OP */
                }
            }
        })
        envelopeViewModel.syncEnvelopeLiveData.observe(viewLifecycleOwner, Observer<SyncEnvelopeModel> { model ->
            when (model.status) {
                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    pendingSyncAdapter?.removeItem(model.position)
                    showSuccessfulSyncDialog(activity, false)
                }

                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TemplatesFragment.TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    /* NO-OP */
                }
            }
        })

        envelopeViewModel.syncAllEnvelopesLiveData.observe(viewLifecycleOwner, Observer<SyncAllEnvelopesModel> { model ->
            when (model.status) {

                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    pendingSyncAdapter?.removeAll()
                    showSuccessfulSyncDialog(activity, true)
                }

                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TemplatesFragment.TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    /* NO-OP */
                }
            }
        })
    }

    private fun toggleProgressBar(isBusy: Boolean) {
        activity?.findViewById<ProgressBar>(R.id.pending_sync_progress_bar)?.visibility = if (isBusy) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun displayError(error: String) {
        activity?.apply {
            val synPendingErrorTextView = findViewById<TextView>(R.id.pending_sync_error_text_view)
            synPendingErrorTextView.visibility = View.VISIBLE
            synPendingErrorTextView.text = error
            val syncAllButton = findViewById<Button>(R.id.sync_all_button)
            syncAllButton.visibility = View.GONE
        }
    }

    private fun showSuccessfulSyncDialog(context: Context?, syncAll: Boolean) {
        val message = if (syncAll) getString(R.string.envelope_sync_all_success_message) else getString(R.string.envelope_sync_success_message)

        AlertDialog.Builder(context)
            .setTitle(getString(R.string.envelope_created_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog: DialogInterface, id: Int ->
                if (pendingSyncAdapter?.getSize() == 0) {
                    displayError(resources.getString(R.string.syn_pending_error))
                }
                dialog.cancel()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
