package com.docusign.sdksamplekotlin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.activity.AgreementActivity
import com.docusign.sdksamplekotlin.model.Client
import com.docusign.sdksamplekotlin.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PortfolioFragment : Fragment() {

    private var client: Client? = null

    interface PortFolioFragmentListener {
        fun displayClientInvestment(client: Client?)
    }

    companion object {
        val TAG = PortfolioFragment::class.java.simpleName

        fun newInstance(client: Client?): PortfolioFragment {
            val bundle = Bundle().apply {
            }
            val fragment = PortfolioFragment()
            fragment.client = client
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.apply {
            client?.let { client ->
                val clientNameTextView = findViewById<TextView>(R.id.name_text_view)
                clientNameTextView.text = client.name
                val clientGraphImageView = findViewById<ImageView>(R.id.client_graph_image_view)
                if (client.storePref == Constants.CLIENT_A_PREF) {
                    clientGraphImageView.setImageResource(R.drawable.portfolio_a)
                } else {
                    clientGraphImageView.setImageResource(R.drawable.portfolio_b)
                }
                val datedTextView = findViewById<TextView>(R.id.dated_text_view)
                val date = Date()
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                datedTextView.text = "Dated: ${dateFormat.format(date)}"
            }
            val viewAgreementButton = findViewById<Button>(R.id.view_agreement_button)
            viewAgreementButton.setOnClickListener {
                (activity as AgreementActivity).displayClientInvestment(client)
            }
        }
    }
}
