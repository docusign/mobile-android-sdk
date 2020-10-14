package com.docusign.sdksamplekotlin.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.exceptions.DSEnvelopeException
import com.docusign.androidsdk.listeners.DSComposeAndSendEnvelopeListener
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.SDKSampleApplication
import com.docusign.sdksamplekotlin.activity.AgreementActivity
import com.docusign.sdksamplekotlin.adapter.AppointmentAdapter
import com.docusign.sdksamplekotlin.livedata.SignOfflineModel
import com.docusign.sdksamplekotlin.livedata.SignOnlineModel
import com.docusign.sdksamplekotlin.livedata.Status
import com.docusign.sdksamplekotlin.model.Appointment
import com.docusign.sdksamplekotlin.utils.EnvelopeUtils
import com.docusign.sdksamplekotlin.utils.SigningType
import com.docusign.sdksamplekotlin.viewmodel.SigningViewModel

class OverviewFragment : Fragment(), AppointmentAdapter.AppointmentListener {

    companion object {
        val TAG = OverviewFragment::class.java.simpleName

        fun newInstance(): OverviewFragment {
            val bundle = Bundle().apply {
            }
            val fragment = OverviewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var signingViewModel: SigningViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            val appointmentRecyclerView = activity.findViewById<RecyclerView>(R.id.appointments_recycler_view)
            appointmentRecyclerView.layoutManager = LinearLayoutManager(activity)
            val appointments = mutableListOf<Appointment>()
            appointments.add(Appointment("Sep 2, 2020", "Tom Wood", false))
            appointments.add(Appointment("Sep 1, 2020", "Andrea G Kuhn", true))
            appointmentRecyclerView.adapter = AppointmentAdapter(appointments, this)
            val createNewAgreementButton = activity.findViewById<Button>(R.id.create_new_agreement_button)
            createNewAgreementButton.setOnClickListener {
                showCreateNewAgreementDialog(activity)
            }
            signingViewModel = SigningViewModel()
            initLiveDataObservers(activity)
        }
    }

    private fun initLiveDataObservers(context: Context) {
        signingViewModel.signOfflineLiveData.observe(viewLifecycleOwner, Observer<SignOfflineModel> { model ->
            when (model.status) {
                Status.START -> {
                    /* NO-OP */
                }
                Status.COMPLETE -> {
                    showSuccessfulSigningDialog(context, SigningType.OFFLINE_SIGNING)
                }
                Status.ERROR -> {
                    model.exception?.let { exception ->
                        Log.d(TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        signingViewModel.signOnlineLiveData.observe(viewLifecycleOwner, Observer<SignOnlineModel> { model ->
            when (model.status) {
                Status.START -> {
                    /* NO-OP */
                }
                Status.COMPLETE -> {
                    showSuccessfulSigningDialog(context, SigningType.ONLINE_SIGNING)
                }
                Status.ERROR -> {
                    model.exception?.let { exception ->
                        Log.d(TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun showCreateNewAgreementDialog(context: Context) {
        val version = DocuSign.getInstance().getSDKVersion()
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.new_agreement_title) + "($version)")
            .setMessage(getString(R.string.new_agreement_message))
            .setPositiveButton(getString(R.string.online_envelope)) { dialog: DialogInterface, id: Int ->
                createEnvelope(context, SigningType.ONLINE_SIGNING)
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.offline_envelope)) { dialog: DialogInterface, id: Int ->
                createEnvelope(context, SigningType.OFFLINE_SIGNING)
                dialog.cancel()
            }
            .setNeutralButton(getString(R.string.cancel)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun createEnvelope(context: Context, signingType: SigningType) {
        val document = (activity?.application as SDKSampleApplication).sampleDoc
        if (document == null) {
            Log.e(TAG, "Unable to retrieve document")
            Toast.makeText(context, "Unable to retrieve document", Toast.LENGTH_LONG).show()
            return
        }
        val envelope = EnvelopeUtils.buildEnvelope(context, document)
        if (envelope == null) {
            Log.e(TAG, "Unable to create envelope")
            Toast.makeText(context, "Unable to create envelope", Toast.LENGTH_LONG).show()
            return
        }
        val envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate()
        envelopeDelegate.composeAndSendEnvelope(envelope, object : DSComposeAndSendEnvelopeListener {

            override fun onSuccess(envelopeId: String, isEnvelopeSent: Boolean) {
                showEnvelopeCreatedDialog(context, signingType, envelopeId)
            }

            override fun onError(exception: DSEnvelopeException) {
                Log.e(TAG, exception.message!!)
                Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showEnvelopeCreatedDialog(context: Context, signingType: SigningType, envelopeId: String) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.envelope_created_title))
            .setMessage(getString(R.string.envelope_created_message))
            .setPositiveButton(getString(R.string.sign)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                if (signingType == SigningType.ONLINE_SIGNING) {
                    signingViewModel.signOnline(context, envelopeId)
                } else if (signingType == SigningType.OFFLINE_SIGNING) {
                    signingViewModel.signOffline(context, envelopeId)
                }
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
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onAppointmentSelected() {
        val intent = Intent(activity, AgreementActivity::class.java)
        activity?.startActivity(intent)
    }
}
