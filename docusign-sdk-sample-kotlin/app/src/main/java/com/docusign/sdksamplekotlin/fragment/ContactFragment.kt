package com.docusign.sdksamplekotlin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.model.Client
import com.docusign.sdksamplekotlin.utils.Constants
import com.google.gson.Gson

class ContactFragment : Fragment() {

    private var client: Client? = null

    companion object {
        val TAG = ContactFragment::class.java.simpleName

        fun newInstance(client: Client?): ContactFragment {
            val bundle = Bundle().apply {
            }
            val fragment = ContactFragment()
            fragment.client = client
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        client?.let { client ->
            activity?.apply {
                val nameTextView = findViewById<EditText>(R.id.client_name_text_view)
                nameTextView.setText(client.name)
                val phoneTextView = findViewById<EditText>(R.id.phone_text_view)
                phoneTextView.setText(client.phone)
                val emailTextView = findViewById<EditText>(R.id.email_text_view)
                emailTextView.setText(client.email)
                val addressLine1TextView = findViewById<EditText>(R.id.address_line1_text_view)
                addressLine1TextView.setText(client.addressLine1)
                val addressLine2TextView = findViewById<EditText>(R.id.address_line2_text_view)
                addressLine2TextView.setText(client.addressLine2)
                val addressLine3TextView = findViewById<EditText>(R.id.address_line3_text_view)
                addressLine3TextView.setText(client.addressLine3)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.apply {
            val nameTextView = findViewById<EditText>(R.id.client_name_text_view)
            val clientName = nameTextView.text.toString()
            val phoneTextView = findViewById<EditText>(R.id.phone_text_view)
            val clientPhone = phoneTextView.text.toString()
            val emailTextView = findViewById<EditText>(R.id.email_text_view)
            val clientEmail = emailTextView.text.toString()
            val addressLine1TextView = findViewById<EditText>(R.id.address_line1_text_view)
            val clientAddressLine1 = addressLine1TextView.text.toString()
            val addressLine2TextView = findViewById<EditText>(R.id.address_line2_text_view)
            val clientAddressLine2 = addressLine2TextView.text.toString()
            val addressLine3TextView = findViewById<EditText>(R.id.address_line3_text_view)
            val clientAddressLine3 = addressLine3TextView.text.toString()
            client?.apply {
                name = clientName
                phone = clientPhone
                email = clientEmail
                addressLine1 = clientAddressLine1
                addressLine2 = clientAddressLine2
                addressLine3 = clientAddressLine3
                val sharedPreferences = context?.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                val clientJson = Gson().toJson(client)
                clientJson?.let {
                    sharedPreferences?.edit()?.putString(storePref, clientJson)?.apply()
                }
            }
        }
    }
}
