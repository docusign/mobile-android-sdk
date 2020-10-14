package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.docusign.sdksamplekotlin.R

class ContactFragment : Fragment() {

    companion object {
        val TAG = ContactFragment::class.java.simpleName

        fun newInstance(): ContactFragment {
            val bundle = Bundle().apply {
            }
            val fragment = ContactFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }
}
