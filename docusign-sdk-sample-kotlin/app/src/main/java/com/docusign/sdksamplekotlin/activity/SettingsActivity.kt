package com.docusign.sdksamplekotlin.activity

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.SDKSampleApplication
import com.docusign.sdksamplekotlin.utils.Constants

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val saveButton = findViewById<Button>(R.id.save_button)
        val sharedPreferences = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val integratorKeyEditText = findViewById<EditText>(R.id.integrator_key_edit_text)
        val clientSecretEditText = findViewById<EditText>(R.id.client_secret_edit_text)
        val redirectURIEditText = findViewById<EditText>(R.id.redirect_uri_edit_text)
        val integratorKey = sharedPreferences.getString(Constants.DOCUSIGN_INTEGRATOR_KEY_PREF, Constants.DOCUSIGN_INTEGRATOR_KEY)
        val clientSecret = sharedPreferences.getString(Constants.CLIENT_SECRET_KEY_PREF, Constants.CLIENT_SECRET_KEY)
        val redirectURI = sharedPreferences.getString(Constants.REDIRECT_URI_PREF, Constants.REDIRECT_URI)
        integratorKeyEditText.setText(integratorKey)
        clientSecretEditText.setText(clientSecret)
        redirectURIEditText.setText(redirectURI)

        saveButton.setOnClickListener {
            if (TextUtils.isEmpty(integratorKeyEditText.text)) {
                Toast.makeText(this, "Please enter Integrator Key", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(clientSecretEditText.text)) {
                Toast.makeText(this, "Please enter Client Secret", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(redirectURIEditText.text)) {
                Toast.makeText(this, "Please enter Redirect Uri", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            sharedPreferences.edit().putString(Constants.DOCUSIGN_INTEGRATOR_KEY_PREF, integratorKeyEditText.text.toString()).apply()
            sharedPreferences.edit().putString(Constants.CLIENT_SECRET_KEY_PREF, clientSecretEditText.text.toString()).apply()
            sharedPreferences.edit().putString(Constants.REDIRECT_URI_PREF, redirectURIEditText.text.toString()).apply()
            (application as SDKSampleApplication).initializeDocuSign()
            finish()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
    }
}
