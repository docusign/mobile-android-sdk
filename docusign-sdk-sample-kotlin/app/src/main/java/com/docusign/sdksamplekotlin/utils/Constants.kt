package com.docusign.sdksamplekotlin.utils

internal object Constants {
    const val DOCUSIGN_INTEGRATOR_KEY = ""  // YOUR DOCUSIGN INTEGRATOR KEY
    const val CLIENT_SECRET_KEY = ""   // YOUR CLIENT SECRET KEY
    const val REDIRECT_URI = ""  // YOUR REDIRECT URI

    const val LOGIN_REQUEST_CODE = 1000
    const val APP_SHARED_PREFERENCES = "AppSharedPreferences"
    const val CLIENT_A_PREF = "client_a_details"
    const val CLIENT_B_PREF = "client_b_details"
    const val CLIENT_C_PREF = "client_c_details"
    const val CLIENT_A_SIGNED_STATUS = "client_a_signed_status"
    const val CLIENT_B_SIGNED_STATUS = "client_b_signed_status"
    const val CLIENT_C_SIGNED_STATUS = "client_c_signed_status"

    const val DOCUSIGN_INTEGRATOR_KEY_PREF = "integrator_key"
    const val CLIENT_SECRET_KEY_PREF = "client_secret"
    const val REDIRECT_URI_PREF = "redirect_uri"


    const val PORTFOLIO_A_DOCUMENT_FILE_NAME = "TGK-Capital-PortfolioA-Agreement.pdf"
    const val PORTFOLIO_B_DOCUMENT_FILE_NAME = "TGK-Capital-PortfolioB-Agreement.pdf"
    const val ACCREDITED_INVESTOR_VERIFICATION_FILE_NAME = "Accredited-Investor-Verification.pdf"
    object TemplateConstants {
        object PortfolioA {
            const val TEMPLATE_ID = "97d25070-57d4-45ae-bc96-e37712f53711"
            const val TAG_DATA_LABEL_ADDRESS_LINE_1 = "Text d7484b70-984b-4aab-a74c-e4583ab7c12c" // Address line 1
            const val TAG_DATA_LABEL_ADDRESS_LINE_2 = "Text 475f30e6-1bcb-41fa-8c79-817cebfd3023" // Address line 2
            const val TAG_DATA_LABEL_ADDRESS_LINE_3 = "Text 3b49cda0-1223-4a70-90d5-96c98cc04dcd" // Address line 3
            const val TAG_DATA_LABEL_FULL_NAME = "Text 2c73d239-b219-4582-a036-1c101ae8902d" // Full name
            const val TAG_DATA_LABEL_CLIENT_NUMBER = "Text 6b33080b-93ec-4bff-85bd-d3820e7c5df6" // Client number
            const val TAG_DATA_LABEL_INVESTMENT_AMOUNT = "Text 32e81242-2d9f-4dc8-8744-5a6bdc1d2af3" // Investment amount
            const val TAG_DATA_LABEL_APPLICANT_NAME = "Text 8683008e-8cfb-4f40-acec-d7d87c699de5" // Name of applicant
        }

        object PortfolioB {
            const val TEMPLATE_ID = "b97df907-5550-4640-92be-6150d5e883a8"
            const val TAG_DATA_LABEL_ADDRESS_LINE_1 = "Text 32ba09ea-b60f-47bd-9b8c-df44c1549498" // Address line 1
            const val TAG_DATA_LABEL_ADDRESS_LINE_2 = "Text e5c9f1fd-6710-4cef-b147-ff9cb5b79fa7" // Address line 2
            const val TAG_DATA_LABEL_ADDRESS_LINE_3 = "Text e239b7e6-d0ce-49f1-9a6d-85b12f11cc3a" // Address line 3
            const val TAG_DATA_LABEL_FULL_NAME = "Text 0d896ae2-aeb9-4488-b3e5-03c9d3ae3baf" // Full name
            const val TAG_DATA_LABEL_CLIENT_NUMBER = "Text 1ae51593-3b3d-4433-a6f7-dc9d964adbf3" // Client number
            const val TAG_DATA_LABEL_INVESTMENT_AMOUNT = "Text d1256a85-f4be-404e-b7c4-e767796e82b6" // Investment amount
            const val TAG_DATA_LABEL_APPLICANT_NAME = "Text fe6fa326-6abb-4ae9-aa93-80d78ce6bd40" // Name of applicant
        }
    }
}
