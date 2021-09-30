package com.docusign.sdksamplekotlin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.activity.AgreementActivity
import com.docusign.sdksamplekotlin.model.Client
import com.docusign.sdksamplekotlin.utils.Constants
import com.google.gson.Gson

class NewPresentationFragment : Fragment() {

    private lateinit var investorNameEditText: EditText

    private lateinit var investorEmailEditText: EditText

    private lateinit var cacheEnvelopeCheckBox: CheckBox

    private lateinit var viewPortfolioButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_presentation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        investorNameEditText = view.findViewById(R.id.investor_name_edit_text)
        investorEmailEditText = view.findViewById(R.id.investor_email_edit_text)
        cacheEnvelopeCheckBox = view.findViewById(R.id.cache_envelope_checkbox)

        viewPortfolioButton = view.findViewById(R.id.view_portfolio_button)
        viewPortfolioButton.setOnClickListener {
            val investorName = investorNameEditText.text.toString()
            val investorEmail = investorEmailEditText.text.toString()

            val error = investorName.isEmpty() || investorEmail.isEmpty()

            if (investorName.isEmpty())
                investorNameEditText.error = getString(R.string.investor_name_empty)

            if (investorEmail.isEmpty())
                investorEmailEditText.error = getString(R.string.investor_email_empty)

            if (error)
                return@setOnClickListener

            val client = Client(
                "FA-45231-007",
                investorName,
                "415-555-1236",
                investorEmail,
                "W Chalmers Pl",
                "Chicago, IL",
                "USA - 60614",
                "$100,000",
                Constants.CLIENT_C_PREF,
                cacheEnvelopeCheckBox.isChecked
            )

            val intent = Intent(requireContext(), AgreementActivity::class.java)
            val clientJson = Gson().toJson(client)
            clientJson?.let {
                intent.putExtra(AgreementActivity.CLIENT_DETAILS, clientJson)
                intent.putExtra(AgreementActivity.OPEN_CLIENT_INVESTMENT, true)
            }
            startActivity(intent)
        }
    }

    companion object {
        val TAG: String = NewPresentationFragment::class.java.simpleName

        fun newInstance() = NewPresentationFragment()
    }
}