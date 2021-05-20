package com.docusign.sdksamplekotlin.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import com.docusign.androidsdk.DocuSign
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.livedata.Status
import com.docusign.sdksamplekotlin.livedata.UseTemplateOfflineModel
import com.docusign.sdksamplekotlin.livedata.UseTemplateOnlineModel
import com.docusign.sdksamplekotlin.model.Client
import com.docusign.sdksamplekotlin.utils.EnvelopeUtils
import com.docusign.sdksamplekotlin.utils.SigningType
import com.docusign.sdksamplekotlin.utils.Constants
import com.docusign.sdksamplekotlin.utils.Utils
import com.docusign.sdksamplekotlin.utils.ClientUtils
import com.docusign.sdksamplekotlin.viewmodel.TemplatesViewModel

class AgreementTemplatesFragment : TemplatesFragment() {

    private var client: Client? = null
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        val TAG = AgreementTemplatesFragment::class.java.simpleName

        fun newInstance(client: Client?): AgreementTemplatesFragment {
            val bundle = Bundle().apply {
            }
            val fragment = AgreementTemplatesFragment()
            fragment.client = client
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var templatesViewModel: TemplatesViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        templatesViewModel = TemplatesViewModel()
        sharedPreferences = requireContext().getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        initLiveDataObservers()
    }

    private fun initLiveDataObservers() {
        templatesViewModel.useTemplateOnlineLiveData.observe(viewLifecycleOwner, Observer<UseTemplateOnlineModel> { model ->
            when (model.status) {
                Status.START -> {
                    toggleProgressBar(false)
                }

                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    Log.d(TAG, "Envelope with ${model.envelopeId} is signed online successfully")
                    activity?.let { activity ->
                        showSuccessfulSigningDialog(activity, SigningType.ONLINE_SIGNING)

                        client?.apply {
                            ClientUtils.setSignedStatus(requireContext(), storePref, true)
                        }
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
                    toggleProgressBar(false)
                }

                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    Log.d(TAG, "Envelope with ${model.envelopeId} is signed offline successfully")
                    activity?.let { activity ->
                        showSuccessfulSigningDialog(activity, SigningType.OFFLINE_SIGNING)
                        client?.apply {
                            ClientUtils.setSignedStatus(requireContext(), storePref, true)
                        }
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

    override fun templateSelected(templateId: String, templateName: String?) {
        activity?.let { activity ->
            launchSigning(activity, templateId, templateName)
        }
    }

    private fun launchSigning(context: Context, templateId: String, templateName: String?) {
        val version = DocuSign.getInstance().getSDKVersion()
        Log.d(TAG, "DocuSign SDK version: $version")

        // If you want to prefill template with recipient details, tab details etc, you can set EnvelopeDefaults
        val envelopeDefaults = EnvelopeUtils.buildEnvelopeDefaults(context, templateId, templateName, client?.storePref)
        if (Utils.isNetworkAvailable(context)) {
            toggleProgressBar(true)
            templatesViewModel.useTemplateOnline(context, templateId, envelopeDefaults)
            // templatesViewModel.useTemplateOnline(context, templateId, null)
        } else {
            templatesViewModel.useTemplateOffline(context, templateId, envelopeDefaults)
            // templatesViewModel.useTemplateOffline(context, templateId, null)
        }
    }

    private fun showSuccessfulSigningDialog(context: Context, signingType: SigningType) {
        var message = getString(R.string.envelope_signed_offline_message)
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
