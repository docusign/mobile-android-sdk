package com.docusign.sdksamplekotlin.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.DSEnvelope
import com.docusign.androidsdk.exceptions.DSEnvelopeException
import com.docusign.androidsdk.listeners.DSCacheEnvelopeListener
import com.docusign.androidsdk.listeners.DSComposeAndSendEnvelopeListener
import com.docusign.androidsdk.listeners.DSGetEnvelopeListener
import com.docusign.esign.api.EnvelopesApi
import com.docusign.esign.model.EnvelopeSummary
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.SDKSampleApplication
import com.docusign.sdksamplekotlin.activity.AgreementActivity
import com.docusign.sdksamplekotlin.livedata.*
import com.docusign.sdksamplekotlin.model.AccreditedInvestorVerification
import com.docusign.sdksamplekotlin.model.AccreditedInvestorVerifier
import com.docusign.sdksamplekotlin.model.Client
import com.docusign.sdksamplekotlin.utils.*
import com.docusign.sdksamplekotlin.viewmodel.SigningViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ClientInvestmentFragment : Fragment() {

    private var client: Client? = null
    private lateinit var signingViewModel: SigningViewModel

    interface ClientInvestmentFragmentListener {
        fun displayAgreementTemplates(client: Client?)
    }

    companion object {
        val TAG = ClientInvestmentFragment::class.java.simpleName

        fun newInstance(client: Client?): ClientInvestmentFragment {
            val bundle = Bundle().apply {
            }
            val fragment = ClientInvestmentFragment()
            fragment.client = client
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_client_investment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.apply {
            signingViewModel = SigningViewModel()
            initLiveDataObservers(this)
            val clientGraphImageView = findViewById<ImageView>(R.id.client_graph_growth_image_view)
            val investmentAmountSpinner = findViewById<Spinner>(R.id.invest_amount_spinner)
            val investments = arrayOf("$300,000", "$400,000", "$500,000")
            val investmentSpinnerAdapter = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                investments
            )
            val accreditedInvestorCheckbox =
                findViewById<CheckBox>(R.id.accredited_investor_checkbox)
            investmentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            investmentAmountSpinner.adapter = investmentSpinnerAdapter
            if (client?.storePref == Constants.CLIENT_B_PREF) {
                if (investments.isNotEmpty()) {
                    investmentAmountSpinner.setSelection(investments.size - 1)
                    val accreditedInvestorTextView =
                        findViewById<TextView>(R.id.accredited_investor_text_view)
                    accreditedInvestorTextView.visibility = View.VISIBLE
                    accreditedInvestorCheckbox.visibility = View.VISIBLE
                    accreditedInvestorCheckbox.isChecked = true
                }
            }
            investmentAmountSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        /* NO-OP */
                    }

                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val clientGraphs = arrayOf(
                            R.drawable.growth_portfolio_a_0,
                            R.drawable.growth_portfolio_a_1,
                            R.drawable.growth_portfolio_a_2
                        )
                        if (position < clientGraphs.size) {
                            clientGraphImageView.setImageResource(clientGraphs[position])
                            client?.apply {
                                investmentAmount = investments[position]
                                val sharedPreferences = context?.getSharedPreferences(
                                    Constants.APP_SHARED_PREFERENCES,
                                    Context.MODE_PRIVATE
                                )
                                val clientJson = Gson().toJson(client)
                                clientJson?.let {
                                    sharedPreferences?.edit()?.putString(storePref, clientJson)
                                        ?.apply()
                                }
                                accreditedInvestorCheckbox.isChecked =
                                    position == investments.size - 1
                            }
                        }
                    }
                }
            val investButton = findViewById<Button>(R.id.invest_button)
            investButton.setOnClickListener {
                when (client?.storePref) {
                    Constants.CLIENT_A_PREF -> {
                        // Launch template flow
                        (activity as AgreementActivity).displayAgreementTemplates(client)
                    }
                    Constants.CLIENT_B_PREF -> {
                        // Launch envelope flow
                        createEnvelope(
                            this,
                            accreditedInvestorCheckbox.isChecked,
                            client?.storePref
                        )
                    }
                    Constants.CLIENT_C_PREF -> {
                        if (client?.cacheEnvelope == true) {
                            cachedEnvelope(requireContext(), client?.storePref)
                        } else {
                            captiveSigning(this, client?.storePref)
                        }
                    }
                }
            }
        }
    }

    private fun cachedEnvelope(context: Context, clientPref: String?) {

        val envelopeDefinition =
            EnvelopeUtils.buildCachedEnvelopeDefinition(clientPref!!, requireActivity())

        val envelopesApi =
            DocuSign.getInstance().getESignApiDelegate().createApiService(EnvelopesApi::class.java)

        val authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate()
        val user = authenticationDelegate.getLoggedInUser(context)
        val call = envelopesApi?.envelopesPostEnvelopes(
            user.accountId,
            null,
            null,
            null,
            null,
            null,
            envelopeDefinition
        )

        val envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate()

        call?.enqueue(object : Callback<EnvelopeSummary> {
            override fun onResponse(
                call: Call<EnvelopeSummary>,
                response: Response<EnvelopeSummary>
            ) {
                if (response.isSuccessful) {
                    envelopeDelegate.cacheEnvelope(
                        response.body()?.envelopeId ?: "",
                        object : DSCacheEnvelopeListener {
                            override fun onComplete(envelope: DSEnvelope) {
                                signingViewModel.signCachedEnvelope(context, envelope.envelopeId)
                            }

                            override fun onError(exception: DSEnvelopeException) {
                                Log.d(OverviewFragment.TAG, exception.message!!)
                                Toast.makeText(activity, exception.message, Toast.LENGTH_LONG)
                                    .show()
                            }

                            override fun onStart() {
                                toggleProgressBar(true)
                            }
                        })
                }
            }

            override fun onFailure(call: Call<EnvelopeSummary>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun initLiveDataObservers(context: Context) {
        signingViewModel.signOfflineLiveData.observe(
            viewLifecycleOwner,
            Observer<SignOfflineModel> { model ->
                when (model.status) {
                    Status.START -> {
                        toggleProgressBar(false)
                    }
                    Status.COMPLETE -> {
                        toggleProgressBar(false)
                        showSuccessfulSigningDialog(context, SigningType.OFFLINE_SIGNING)
                        client?.apply {
                            ClientUtils.setSignedStatus(requireContext(), storePref, true)
                        }
                    }
                    Status.ERROR -> {
                        toggleProgressBar(false)
                        model.exception?.let { exception ->
                            Log.d(OverviewFragment.TAG, exception.message!!)
                            Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            })
        signingViewModel.signOnlineLiveData.observe(
            viewLifecycleOwner,
            Observer<SignOnlineModel> { model ->
                when (model.status) {
                    Status.START -> {
                        toggleProgressBar(false)
                    }
                    Status.COMPLETE -> {
                        toggleProgressBar(false)
                        showSuccessfulSigningDialog(context, SigningType.ONLINE_SIGNING)
                        client?.apply {
                            ClientUtils.setSignedStatus(requireContext(), storePref, true)
                        }
                    }
                    Status.ERROR -> {
                        toggleProgressBar(false)
                        model.exception?.let { exception ->
                            Log.d(OverviewFragment.TAG, exception.message!!)
                            Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            })
        signingViewModel.captiveSigningLiveData.observe(
            viewLifecycleOwner,
            Observer<CaptiveSigningModel> { model ->
                when (model.status) {
                    Status.START -> {
                        toggleProgressBar(false)
                    }
                    Status.COMPLETE -> {
                        toggleProgressBar(false)
                        showSuccessfulSigningDialog(context, SigningType.CAPTIVE_SIGNING)
                        client?.apply {
                            ClientUtils.setSignedStatus(requireContext(), storePref, true)
                        }
                    }
                    Status.ERROR -> {
                        toggleProgressBar(false)
                        model.exception?.let { exception ->
                            Log.d(OverviewFragment.TAG, exception.message!!)
                            Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            })
        signingViewModel.cachedEnvelopeSigningLiveData.observe(
            viewLifecycleOwner,
            Observer<CachedEnvelopeSigningModel> { model ->
                when (model.status) {
                    Status.START -> {
                        toggleProgressBar(false)
                    }
                    Status.COMPLETE -> {
                        toggleProgressBar(false)
                        showSuccessfulSigningDialog(context, SigningType.OFFLINE_SIGNING)
                        client?.apply {
                            ClientUtils.setSignedStatus(requireContext(), storePref, true)
                        }
                    }
                    Status.ERROR -> {
                        toggleProgressBar(false)
                        model.exception?.let { exception ->
                            Log.d(OverviewFragment.TAG, exception.message!!)
                            Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            })
    }

    private fun toggleProgressBar(isBusy: Boolean) {
        activity?.findViewById<ProgressBar>(R.id.envelopes_progress_bar)?.visibility = if (isBusy) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun captiveSigning(context: Context, clientPref: String?) {
        val envelopeDefinition =
            EnvelopeUtils.buildEnvelopeDefinition(clientPref!!, requireActivity())

        val envelopesApi =
            DocuSign.getInstance().getESignApiDelegate().createApiService(EnvelopesApi::class.java)

        val authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate()
        val user = authenticationDelegate.getLoggedInUser(context)
        val call = envelopesApi?.envelopesPostEnvelopes(
            user.accountId,
            null,
            null,
            null,
            null,
            null,
            envelopeDefinition
        )

        val envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate()

        call?.enqueue(object : Callback<EnvelopeSummary> {
            override fun onResponse(
                call: Call<EnvelopeSummary>,
                response: Response<EnvelopeSummary>
            ) {
                if (response.isSuccessful) {
                    val envelopeSummary = response.body()
                    envelopeSummary?.let {
                        envelopeDelegate.getEnvelope(it.envelopeId, object : DSGetEnvelopeListener {
                            override fun onError(exception: DSEnvelopeException) {
                                Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                            }

                            override fun onSuccess(envelope: DSEnvelope) {
                                signingViewModel.captiveSigning(requireContext(), envelope)
                            }
                        })
                    }

                }
            }

            override fun onFailure(call: Call<EnvelopeSummary>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createEnvelope(
        context: Context,
        isAccreditedInvestor: Boolean,
        clientPref: String?
    ) {
        val document =
            if (clientPref == Constants.CLIENT_A_PREF) (activity?.application as SDKSampleApplication).portfolioADoc else
                (activity?.application as SDKSampleApplication).portfolioBDoc

        if (document == null) {
            Log.e(OverviewFragment.TAG, "Unable to retrieve document")
            Toast.makeText(context, "Unable to retrieve document", Toast.LENGTH_LONG).show()
            return
        }
        var accreditedInvestorVerification: AccreditedInvestorVerification? = null

        if (isAccreditedInvestor) {
            var accreditedInvestorVerificationDocument: File? = null
            accreditedInvestorVerificationDocument =
                (activity?.application as SDKSampleApplication).accreditedInvestorDoc

            if (accreditedInvestorVerificationDocument == null) {
                Toast.makeText(
                    context,
                    "AccreditedInvestor Verification document is not available",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            val sharedPreferences =
                context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val clientString = sharedPreferences.getString(clientPref, null)

            if (clientString == null) {
                Toast.makeText(context, "Client details not available", Toast.LENGTH_LONG).show()
                return
            }

            val client = Gson().fromJson<Client>(clientString, Client::class.java)
            val clientAddress =
                client.addressLine1 + "," + client.addressLine2 + "," + client.addressLine3
            val authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate()
            val user = authenticationDelegate.getLoggedInUser(context)
            val accreditedInvestorVerifier = AccreditedInvestorVerifier(
                user.name,
                getString(R.string.tgkfinancial), "TN12345", "CA",
                "202 Main Street", null, "San Francisco, CA, 94105"
            )
            accreditedInvestorVerification =
                AccreditedInvestorVerification(
                    client.name,
                    clientAddress,
                    accreditedInvestorVerifier,
                    accreditedInvestorVerificationDocument
                )

        }
        val envelope = EnvelopeUtils.buildEnvelope(
            context,
            document,
            accreditedInvestorVerification,
            clientPref
        )
        if (envelope == null) {
            Log.e(OverviewFragment.TAG, "Unable to create envelope")
            Toast.makeText(context, "Unable to create envelope", Toast.LENGTH_LONG).show()
            return
        }
        val envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate()
        envelopeDelegate.composeAndSendEnvelope(
            envelope,
            object : DSComposeAndSendEnvelopeListener {

                override fun onSuccess(envelopeId: String, isEnvelopeSent: Boolean) {
                    if (Utils.isNetworkAvailable(context)) {
                        toggleProgressBar(true)
                        signingViewModel.signOnline(context, envelopeId)
                    } else {
                        signingViewModel.signOffline(context, envelopeId)
                    }
                }

                override fun onError(exception: DSEnvelopeException) {
                    Log.e(OverviewFragment.TAG, exception.message!!)
                    Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun showSuccessfulSigningDialog(context: Context, signingType: SigningType) {
        var message = getString(R.string.envelope_signed_offline_message)
        if (signingType == SigningType.ONLINE_SIGNING || signingType == SigningType.CAPTIVE_SIGNING) {
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
