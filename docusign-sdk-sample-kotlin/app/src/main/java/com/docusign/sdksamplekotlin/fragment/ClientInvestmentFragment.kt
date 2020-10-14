package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.activity.AgreementActivity

class ClientInvestmentFragment : Fragment() {

    interface ClientInvestmentFragmentListener {
        fun displayAgreementTemplates()
    }

    companion object {
        val TAG = ClientInvestmentFragment::class.java.simpleName

        fun newInstance(): ClientInvestmentFragment {
            val bundle = Bundle().apply {
            }
            val fragment = ClientInvestmentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_client_investment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {activity ->
            val nextButton = activity.findViewById<Button>(R.id.next_button)
            nextButton.setOnClickListener {
                (activity as AgreementActivity).displayAgreementTemplates()
            }
        }
    }
}
