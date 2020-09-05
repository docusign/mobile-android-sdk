package com.docusign.sdksamplekotlin.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.exceptions.DSAuthenticationException
import com.docusign.androidsdk.exceptions.DocuSignNotInitializedException
import com.docusign.androidsdk.listeners.DSAuthenticationListener
import com.docusign.androidsdk.models.DSUser
import com.docusign.androidsdk.util.DSLog
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
    }

    private fun loginToDocuSign() {
        val clientSecretKey = DocuSign.getInstance().getClientSecret()
        val integratorKey = DocuSign.getInstance().getIntegratorKey()
        val redirectURI = DocuSign.getInstance().getRedirectUri()

        if (TextUtils.isEmpty(clientSecretKey)) {
            Toast.makeText(applicationContext, "Please update Client Secret Key in Constants.kt", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(integratorKey)) {
            Toast.makeText(applicationContext, "Please update DocuSIgn Integrator Key in Constants.kt", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(redirectURI)) {
            Toast.makeText(applicationContext, "Please update Redirrect URI in Constants.kt", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val authDelegate =
                DocuSign.getInstance().getAuthenticationDelegate()
            authDelegate.login(Constants.LOGIN_REQUEST_CODE, this,
                object : DSAuthenticationListener {
                    override fun onSuccess(@NonNull user: DSUser) {
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onError(@NonNull exception: DSAuthenticationException) {
                        DSLog.d(TAG, exception)
                        Toast.makeText(applicationContext, "Failed to Login to DocuSign", Toast.LENGTH_LONG).show()
                    }
                }
            )
        } catch (exception: DocuSignNotInitializedException) {
            DSLog.d(TAG, exception)
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

}