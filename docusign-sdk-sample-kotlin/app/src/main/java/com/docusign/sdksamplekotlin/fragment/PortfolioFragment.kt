package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.activity.AgreementActivity

class PortfolioFragment : Fragment() {

    interface PortFolioFragmentListener {
        fun displayClientInvestment()
    }

    companion object {
        val TAG = PortfolioFragment::class.java.simpleName

        fun newInstance(): PortfolioFragment {
            val bundle = Bundle().apply {
            }
            val fragment = PortfolioFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {activity ->
            val viewAgreementButton = activity.findViewById<Button>(R.id.view_agreement_button)
            viewAgreementButton.setOnClickListener {
                (activity as AgreementActivity).displayClientInvestment()
            }
        }
    }
}
