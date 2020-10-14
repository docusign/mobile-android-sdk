package com.docusign.sdksamplekotlin.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.exceptions.DSException
import com.docusign.androidsdk.exceptions.DSSyncException
import com.docusign.androidsdk.listeners.DSSyncAllEnvelopesListener
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.livedata.Status
import com.docusign.sdksamplekotlin.livedata.UseTemplateOfflineModel
import com.docusign.sdksamplekotlin.livedata.UseTemplateOnlineModel
import com.docusign.sdksamplekotlin.utils.EnvelopeUtils
import com.docusign.sdksamplekotlin.utils.SigningType
import com.docusign.sdksamplekotlin.viewmodel.TemplatesViewModel

class AgreementTemplatesFragment : TemplatesFragment() {

    companion object {
        val TAG = AgreementTemplatesFragment::class.java.simpleName

        fun newInstance(): AgreementTemplatesFragment {
            val bundle = Bundle().apply {
            }
            val fragment = AgreementTemplatesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var templatesViewModel: TemplatesViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        templatesViewModel = TemplatesViewModel()
        initLiveDataObservers()
    }

    private fun initLiveDataObservers() {
        templatesViewModel.useTemplateOnlineLiveData.observe(viewLifecycleOwner, Observer<UseTemplateOnlineModel> { model ->
            when (model.status) {
                Status.START -> {
                    toggleProgressBar(true)
                }

                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    Log.d(TAG, "Envelope with ${model.envelopeId} is signed online successfully")
                    activity?.let { activity ->
                        showSuccessfulSigningDialog(activity, SigningType.ONLINE_SIGNING)
                    }
                }

                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TemplatesFragment.TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        templatesViewModel.useTemplateOfflineLiveData.observe(viewLifecycleOwner, Observer<UseTemplateOfflineModel> { model ->
            when (model.status) {
                Status.START -> {
                    toggleProgressBar(true)
                }

                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    Log.d(TAG, "Envelope with ${model.envelopeId} is signed offline successfully")
                    activity?.let { activity ->
                        val envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate()
                        envelopeDelegate.syncAllEnvelopes(object : DSSyncAllEnvelopesListener {

                            override fun onStart() {
                                /* NO-OP */
                            }

                            override fun onComplete(failedEnvelopeIdList: List<String>?) {
                                showSuccessfulSigningDialog(activity, SigningType.OFFLINE_SIGNING)
                            }

                            override fun onEnvelopeSyncError(exception: DSSyncException, localEnvelopeId: String, syncRetryCount: Int?) {
                                /* NO-OP */
                            }

                            override fun onEnvelopeSyncSuccess(localEnvelopeId: String, serverEnvelopeId: String?) {
                                /* NO-OP */
                            }

                            override fun onError(exception: DSException) {
                                Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
                            }
                        }, true)
                    }
                }

                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TemplatesFragment.TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun toggleProgressBar(isBusy: Boolean) {
        activity?.findViewById<ProgressBar>(R.id.templates_progress_bar)?.visibility = if (isBusy) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun templateSelected(templateId: String) {
        activity?.let { activity ->
            showCreateNewAgreementDialog(activity, templateId)
        }
    }

    private fun showCreateNewAgreementDialog(context: Context, templateId: String) {
        val version = DocuSign.getInstance().getSDKVersion()
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.new_agreement_title) + "($version)")
            .setMessage(getString(R.string.new_agreement_template_message))
            .setPositiveButton(getString(R.string.online_envelope)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                // If you want to prefill template with recipient details, tab details etc, you can set EnvelopeDefaults
                // val envelopeDefaults = EnvelopeUtils.buildEnvelopeDefaults(context)
                // templatesViewModel.useTemplateOnline(context, templateId, envelopeDefaults)
                templatesViewModel.useTemplateOnline(context, templateId, null)
            }
            .setNegativeButton(getString(R.string.offline_envelope)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                // If you want to prefill template with recipient details, tab details etc, you can set EnvelopeDefaults
                 // val envelopeDefaults = EnvelopeUtils.buildEnvelopeDefaults(context)
                 // templatesViewModel.useTemplateOffline(context, templateId, envelopeDefaults)
                templatesViewModel.useTemplateOffline(context, templateId, null)
            }
            .setNeutralButton(getString(R.string.cancel)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showSuccessfulSigningDialog(context: Context, signingType: SigningType) {
        var message = getString(R.string.envelope_signed_synced_message)
        if (signingType == SigningType.ONLINE_SIGNING) {
            message = getString(R.string.envelope_signed_message)
        }
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.envelope_created_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                activity?.finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
