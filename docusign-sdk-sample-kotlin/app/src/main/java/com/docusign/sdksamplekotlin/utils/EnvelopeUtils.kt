package com.docusign.sdksamplekotlin.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.DSEnvelope
import com.docusign.androidsdk.dsmodels.DSDocument
import com.docusign.androidsdk.dsmodels.DSTab
import com.docusign.androidsdk.dsmodels.DSEnvelopeRecipient
import com.docusign.androidsdk.dsmodels.DSRecipientType
import com.docusign.androidsdk.dsmodels.DSTabType
import com.docusign.androidsdk.dsmodels.DSTextCustomField
import com.docusign.androidsdk.dsmodels.DSRecipientDefault
import com.docusign.androidsdk.dsmodels.DSEnvelopeDefaults
import com.docusign.androidsdk.dsmodels.DSListCustomField
import com.docusign.androidsdk.dsmodels.DSCustomFields
import com.docusign.androidsdk.dsmodels.DSEnvelopeDefaultsUniqueRecipientSelectorType
import com.docusign.androidsdk.exceptions.DSAuthenticationException
import com.docusign.androidsdk.exceptions.DSEnvelopeException
import com.docusign.androidsdk.exceptions.DocuSignNotInitializedException
import com.docusign.sdksamplekotlin.model.AccreditedInvestorVerification
import com.docusign.sdksamplekotlin.model.Client
import com.google.gson.Gson
import java.io.File
import java.net.URI
import java.util.ArrayList

object EnvelopeUtils {

    private val TAG = EnvelopeUtils::class.java.simpleName

    fun buildEnvelope(
        context: Context,
        file: File,
        accreditedInvestorVerification: AccreditedInvestorVerification?,
        clientPref: String?
    ): DSEnvelope? {

        val sharedPreferences = context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        if (clientPref == null) {
            Toast.makeText(context, "Client Preference not available", Toast.LENGTH_LONG).show()
            return null
        }
        val clientString = sharedPreferences.getString(clientPref, null)

        if (clientString == null) {
            Toast.makeText(context, "Client details not available", Toast.LENGTH_LONG).show()
            return null
        }

        val client = Gson().fromJson<Client>(clientString, Client::class.java)
        try {
            val authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate()
            val user = authenticationDelegate.getLoggedInUser(context)
            val fileURI: URI = file.toURI()
            val documents = mutableListOf<DSDocument>()
            val document = DSDocument.Builder()
                .documentId(1)
                .uri(fileURI.toString())
                .name("TGK Capital Portfolio B Agreement")
                .build()
            documents.add(document)
            val tabs = createInvestmentAgreementTabs(client)

            val recipients = mutableListOf<DSEnvelopeRecipient>()
            recipients.add(
                DSEnvelopeRecipient.Builder()
                    .recipientId(1)
                    .routingOrder(1)
                    .hostName(user.name)
                    .hostEmail(user.email)
                    .signerName(client.name)
                    .signerEmail(client.email)
                    .type(DSRecipientType.IN_PERSON_SIGNER)
                    .tabs(tabs)
                    .build()
            )

            var value = 0

            accreditedInvestorVerification?.let {
                value = 1
                val accreditedInvestorVerificationFileURI: URI = accreditedInvestorVerification.file.toURI()
                val accreditedInvestorVerificationDocument = DSDocument.Builder()
                    .documentId(2)
                    .uri(accreditedInvestorVerificationFileURI.toString())
                    .name("TGK Capital Portfolio B Agreement")
                    .build()
                documents.add(accreditedInvestorVerificationDocument)
                val accreditedInvestorVerificationTabs = createAccreditedInvestorVerificationTabs(accreditedInvestorVerification)
                recipients.add(
                    DSEnvelopeRecipient.Builder()
                        .recipientId(2)
                        .routingOrder(2)
                        .hostName(user.name)
                        .hostEmail(user.email)
                        .signerName(user.name)
                        .signerEmail(user.email)
                        .type(DSRecipientType.IN_PERSON_SIGNER)
                        .tabs(accreditedInvestorVerificationTabs)
                        .build()
                )
            }
            recipients.add(
                DSEnvelopeRecipient.Builder()
                    .recipientId(2 + value.toLong())
                    .routingOrder(2 + value)
                    .signerName("Jack Doe") // if someone needs a signed copy, their name here
                    .signerEmail("j.d@gmail.com") // if someone needs a signed copy, their email here
                    .type(DSRecipientType.CARBON_COPY)
                    .build()
            )
            return DSEnvelope.Builder()
                .envelopeName("TGK Capital Portfolio B Agreement")
                .documents(documents)
                .recipients(recipients)
                .textCustomFields( // this is for free-form metadata
                    getTextCustomFields(client)!!
                )
                .build()
        } catch (exception: DSEnvelopeException) {
            Log.e(TAG, exception.message!!)
        } catch (exception: DocuSignNotInitializedException) {
            Log.e(TAG, exception.message!!)
        } catch (exception: DSAuthenticationException) {
            Log.e(TAG, exception.message!!)
        }
        return null
    }

    private fun createAccreditedInvestorVerificationTabs(accreditedInvestorVerification: AccreditedInvestorVerification): List<DSTab> {
        val tabs = mutableListOf<DSTab>()
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(470)
                .yPosition(25)
                .type(DSTabType.DATE_SIGNED)
                .optional(true)
                .build()
        )                      // Date
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(130)
                .yPosition(50)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.clientName)
                .optional(true)
                .build()
        )                      // Client name
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(130)
                .yPosition(80)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.clientAddress)
                .optional(true)
                .build()
        )                      // Client address
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(220)
                .yPosition(140)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.verifier.name)
                .optional(true)
                .build()
        )                      // Verifier name
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(150)
                .yPosition(250)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.verifier.licenseNumber)
                .optional(true)
                .build()
        )                      // Verifier License Number
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(480)
                .yPosition(250)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.verifier.stateRegistered)
                .optional(true)
                .build()
        )                      // Verifier State Registered
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(50)
                .yPosition(560)
                .type(DSTabType.SIGNATURE)
                .optional(false)
                .build()
        )                      // Verifier Signature
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(50)
                .yPosition(620)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.verifier.name)
                .optional(true)
                .build()
        )                      // Verifier Name
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(50)
                .yPosition(660)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.verifier.company)
                .optional(true)
                .build()
        )                      // Verifier Company
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(320)
                .yPosition(570)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.verifier.addressLine1)
                .optional(true)
                .build()
        )                      // Verifier AddressLine1
        accreditedInvestorVerification.verifier.addressLine2?.let {
            tabs.add(
                DSTab.Builder()
                    .documentId(2)
                    .recipientId(2)
                    .pageNumber(1)
                    .xPosition(320)
                    .yPosition(620)
                    .type(DSTabType.TEXT)
                    .value(accreditedInvestorVerification.verifier.addressLine2)
                    .optional(true)
                    .build()
            )                      // Verifier AddressLine2
        }
        tabs.add(
            DSTab.Builder()
                .documentId(2)
                .recipientId(2)
                .pageNumber(1)
                .xPosition(320)
                .yPosition(660)
                .type(DSTabType.TEXT)
                .value(accreditedInvestorVerification.verifier.addressLine3)
                .optional(true)
                .build()
        )                      // Verifier AddressLine3
        return tabs
    }

    private fun createInvestmentAgreementTabs(client: Client): List<DSTab> {
        val tabs = mutableListOf<DSTab>()
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(370)
                .yPosition(110)
                .type(DSTabType.TEXT)
                .value(client.addressLine1)
                .optional(true)
                .build()
        )                      // Address line 1
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(370)
                .yPosition(140)
                .type(DSTabType.TEXT)
                .value(client.addressLine2)
                .optional(true)
                .build()
        )                        // Address line 2
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(370)
                .yPosition(165)
                .type(DSTabType.TEXT)
                .value(client.addressLine3)
                .optional(true)
                .build()
        )                      // Address line 3
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(50)
                .yPosition(165)
                .type(DSTabType.TEXT)
                .value(client.name)
                .optional(true)
                .build()
        )                       // Full name
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(370)
                .yPosition(550)
                .type(DSTabType.TEXT)
                .value(client.id)
                .optional(true)
                .build()
        )                        // Client number
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(370)
                .yPosition(630)
                .type(DSTabType.TEXT)
                .value(client.investmentAmount)
                .optional(true)
                .build()
        )                        // Investment amount
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(370)
                .yPosition(720)
                .type(DSTabType.SIGNATURE)
                .build()
        )                        // Signature
        tabs.add(
            DSTab.Builder()
                .documentId(1)
                .recipientId(1)
                .pageNumber(1)
                .xPosition(50)
                .yPosition(730)
                .type(DSTabType.TEXT)
                .value(client.name)
                .optional(true)
                .build()
        )                        // Name of the applicant
        return tabs
    }

    private fun getTextCustomFields(client: Client): List<DSTextCustomField>? {
        val textCustomField1: DSTextCustomField
        val textCustomFields: MutableList<DSTextCustomField> = ArrayList()
        try {
            textCustomField1 = DSTextCustomField.Builder()
                .fieldId(123)
                .name("Phone number")
                .value(client.phone)
                .build()
            textCustomFields.add(textCustomField1)
        } catch (exception: DSEnvelopeException) {
            Log.e(TAG, exception.message!!)
        }
        return textCustomFields
    }

    fun buildEnvelopeDefaults(context: Context, templateId: String, templateName: String?, clientPref: String?): DSEnvelopeDefaults? {

        val sharedPreferences = context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        if (clientPref == null) {
            Toast.makeText(context, "Client Preference not available", Toast.LENGTH_LONG).show()
            return null
        }
        val clientString = sharedPreferences.getString(clientPref, null)

        if (clientString == null) {
            Toast.makeText(context, "Client details not available", Toast.LENGTH_LONG).show()
            return null
        }
        val client = Gson().fromJson<Client>(clientString, Client::class.java)

        val recipientDefaults = ArrayList<DSRecipientDefault>()
        val authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate()
        val user = authenticationDelegate.getLoggedInUser(context)


        val emailBlurb = ""
        val envelopeTitle = "Investment Agreement"

        // In person signer
        val recipient1 = DSRecipientDefault(
            user.email,
            user.name,
            client.name,
            client.email,
            null,
            "IPS1",  // This should match the ROLE NAME in the template in DocuSign web portal
            DSEnvelopeDefaultsUniqueRecipientSelectorType.ROLE_NAME
        )

        // Receives carbon copy
        val recipient2 = DSRecipientDefault(
            "j.d@gmail.com",
            "Jack Doe",
            null,
            null,
            null,
            "CC1",  // This should match the ROLE NAME in the template in DocuSign web portal
            DSEnvelopeDefaultsUniqueRecipientSelectorType.ROLE_NAME
        )

        recipientDefaults.add(recipient1)
        recipientDefaults.add(recipient2)

        val isPortfolioA = templateId == Constants.TemplateConstants.PortfolioA.TEMPLATE_ID

        val tabValueDefaults = hashMapOf<String, String>()
        // 'Text 6ffeb574-5e0f-4aa0-a2f6-7c402db4045e' is retrieved from 'Data Label' attribute in template document in DocuSign web portal

        val tagLabelAddressLine1 =
            if (isPortfolioA) Constants.TemplateConstants.PortfolioA.TAG_DATA_LABEL_ADDRESS_LINE_1
            else Constants.TemplateConstants.PortfolioB.TAG_DATA_LABEL_ADDRESS_LINE_1
        val tagLabelAddressLine2 =
            if (isPortfolioA) Constants.TemplateConstants.PortfolioA.TAG_DATA_LABEL_ADDRESS_LINE_2
            else Constants.TemplateConstants.PortfolioB.TAG_DATA_LABEL_ADDRESS_LINE_2
        val tagLabelAddressLine3 =
            if (isPortfolioA) Constants.TemplateConstants.PortfolioA.TAG_DATA_LABEL_ADDRESS_LINE_3
            else Constants.TemplateConstants.PortfolioB.TAG_DATA_LABEL_ADDRESS_LINE_3
        val tagLabelFullName =
            if (isPortfolioA) Constants.TemplateConstants.PortfolioA.TAG_DATA_LABEL_FULL_NAME
            else Constants.TemplateConstants.PortfolioB.TAG_DATA_LABEL_FULL_NAME
        val tagLabelClientNumber =
            if (isPortfolioA) Constants.TemplateConstants.PortfolioA.TAG_DATA_LABEL_CLIENT_NUMBER
            else Constants.TemplateConstants.PortfolioB.TAG_DATA_LABEL_CLIENT_NUMBER
        val tagLabelInvestmentAmount =
            if (isPortfolioA) Constants.TemplateConstants.PortfolioA.TAG_DATA_LABEL_INVESTMENT_AMOUNT
            else Constants.TemplateConstants.PortfolioB.TAG_DATA_LABEL_INVESTMENT_AMOUNT
        val tagLabelApplicantName =
            if (isPortfolioA) Constants.TemplateConstants.PortfolioA.TAG_DATA_LABEL_APPLICANT_NAME
            else Constants.TemplateConstants.PortfolioB.TAG_DATA_LABEL_APPLICANT_NAME

        tabValueDefaults[tagLabelAddressLine1] = client.addressLine1 // Address line 1
        tabValueDefaults[tagLabelAddressLine2] = client.addressLine2 // Address line 2
        tabValueDefaults[tagLabelAddressLine3] = client.addressLine3 // Address line 3
        tabValueDefaults[tagLabelFullName] = client.name // Full name
        tabValueDefaults[tagLabelClientNumber] = client.id // Client number
        tabValueDefaults[tagLabelInvestmentAmount] = client.investmentAmount // Investment amount
        tabValueDefaults[tagLabelApplicantName] = client.name // Name of applicant

        val textCustomFields = getTextCustomFields(client)
        val listCustomFields: MutableList<DSListCustomField> = mutableListOf()
        val customFields = DSCustomFields(listCustomFields, textCustomFields as MutableList<DSTextCustomField>)

        return DSEnvelopeDefaults(
            recipientDefaults,
            templateName,
            emailBlurb,
            envelopeTitle,
            tabValueDefaults,
            customFields
        )
    }
}
