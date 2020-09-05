package com.docusign.sdksamplekotlin

import android.app.Application
import android.widget.Toast
import com.docusign.androidsdk.DSEnvironment
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.exceptions.DocuSignNotInitializedException
import com.docusign.androidsdk.util.DSLog
import com.docusign.androidsdk.util.DSMode
import com.docusign.sdksamplekotlin.utils.Constants

class SDKSampleApplication : Application() {

    companion object {
        val TAG = SDKSampleApplication::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        initializeDocuSign()
    }

    private fun initializeDocuSign() {
        try {
            DocuSign.init(
                applicationContext,
                Constants.DOCUSIGN_INTEGRATOR_KEY,
                Constants.CLIENT_SECRET_KEY,
                Constants.REDIRECT_URI,
                DSMode.DEBUG
            ).setEnvironment(DSEnvironment.DEMO_ENVIRONMENT)
        } catch (exception: DocuSignNotInitializedException) {
            DSLog.d(TAG, exception)
            Toast.makeText(applicationContext, "Failed to Initialize DocuSign. " + exception.message, Toast.LENGTH_LONG).show()
        }
    }
}