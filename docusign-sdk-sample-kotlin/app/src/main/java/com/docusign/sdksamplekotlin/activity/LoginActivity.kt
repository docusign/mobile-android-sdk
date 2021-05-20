package com.docusign.sdksamplekotlin.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.DSUser
import com.docusign.androidsdk.exceptions.DSAuthenticationException
import com.docusign.androidsdk.exceptions.DocuSignNotInitializedException
import com.docusign.androidsdk.listeners.DSAuthenticationListener
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.utils.Constants

class LoginActivity : AppCompatActivity() {

    companion object {
        val TAG = LoginActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            loginToDocuSign()
        }
        title = getString(R.string.login)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_login_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> launchSettings()
        }
        return true
    }

    private fun loginToDocuSign() {
        try {
            val clientSecretKey = DocuSign.getInstance().getClientSecret()
            val integratorKey = DocuSign.getInstance().getIntegratorKey()
            val redirectURI = DocuSign.getInstance().getRedirectUri()

            if (TextUtils.isEmpty(clientSecretKey)) {
                Toast.makeText(applicationContext, "Please provide Client Secret Key", Toast.LENGTH_LONG).show()
                return
            }
            if (TextUtils.isEmpty(integratorKey)) {
                Toast.makeText(applicationContext, "Please provide DocuSIgn Integrator Key", Toast.LENGTH_LONG).show()
                return
            }
            if (TextUtils.isEmpty(redirectURI)) {
                Toast.makeText(applicationContext, "Please provide Redirect URI", Toast.LENGTH_LONG).show()
                return
            }

            val authDelegate = DocuSign.getInstance().getAuthenticationDelegate()
            // DS: Login authentication using OAuth
            authDelegate.login(Constants.LOGIN_REQUEST_CODE, this,
                object : DSAuthenticationListener {
                    override fun onSuccess(@NonNull user: DSUser) {
                        finish()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onError(@NonNull exception: DSAuthenticationException) {
                        Log.d(TAG, exception.message!!)
                        Toast.makeText(applicationContext, "Failed to Login to DocuSign", Toast.LENGTH_LONG).show()
                    }
                }
            )

        } catch (exception: DocuSignNotInitializedException) {
            Log.d(TAG, exception.message!!)
            Toast.makeText(applicationContext, "Failed to Login to DocuSign", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.LOGIN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(applicationContext, "Failed to Login to DocuSign", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun launchSettings() {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
    }
}
