package com.docusign.sdksamplekotlin.utils

import android.content.Context
import android.util.Log
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.dsmodels.*
import com.docusign.androidsdk.exceptions.DSAuthenticationException
import com.docusign.androidsdk.exceptions.DSEnvelopeException
import com.docusign.androidsdk.exceptions.DocuSignNotInitializedException
import com.docusign.sdksamplekotlin.model.Client
import com.google.gson.Gson
import java.io.File
import java.net.URI
import java.util.ArrayList

object EnvelopeUtils {

    private val TAG = EnvelopeUtils::class.java.simpleName

    fun buildEnvelope(context: Context, file: File): DSEnvelope? {
        val sharedPreferences = context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val defaultClientDetails = Client(
            "FA-45231-005", "Tom Wood", "415-555-1234", "tom.wood@digital.com",
            "726 Tennessee St", "San Francisco", "CA 9107", "$25,000"
        )
        val defaultClientDetailsJson = Gson().toJson(defaultClientDetails)
        val clientString = sharedPreferences.getString(Constants.CLIENT_DETAILS_PREF, defaultClientDetailsJson)
        val client = Gson().fromJson<Client>(clientString, Client::class.java)

        try {
            val authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate()
            val user = authenticationDelegate.getLoggedInUser(context)
            val fileURI: URI = file.toURI()
            return DSEnvelope.Builder()
                .envelopeName("Investment Agreement")
                .document(
                    DSDocument.Builder()
                        .documentId(1)
                        .uri(fileURI.toString())
                        .name("Investment Agreement Document")
                        .build()
                )
                .recipient(
                    DSEnvelopeRecipient.Builder()
                        .recipientId(1)
                        .routingOrder(1)
                        .hostName(user.name)
                        .hostEmail(user.email)
                        .signerName(client.name)
                        .signerEmail(client.email)
                        .type(DSRecipientType.IN_PERSON_SIGNER)
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(370)
                                .yPosition(110)
                                .type(DSTabType.TEXT)
                                .value(client.addressLine1)
                                .optional(true)
                                .build()                       // Address line 1
                        )
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(370)
                                .yPosition(140)
                                .type(DSTabType.TEXT)
                                .value(client.addressLine2)
                                .optional(true)
                                .build()                        // Address line 2
                        )
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(370)
                                .yPosition(170)
                                .type(DSTabType.TEXT)
                                .value(client.addressLine3)
                                .optional(true)
                                .build()                        // Address line 2
                        )
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(50)
                                .yPosition(170)
                                .type(DSTabType.TEXT)
                                .value(client.name)
                                .optional(true)
                                .build()                        // Full name
                        )
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(370)
                                .yPosition(570)
                                .type(DSTabType.TEXT)
                                .value(client.id)
                                .optional(true)
                                .build()                        // Client number
                        )
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(370)
                                .yPosition(660)
                                .type(DSTabType.TEXT)
                                .value(client.investmentAmount)
                                .optional(true)
                                .build()                        // Investment amount
                        )
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(370)
                                .yPosition(740)
                                .type(DSTabType.SIGNATURE)
                                .build()                        // Signature
                        )
                        .tab(
                            DSTab.Builder()
                                .documentId(1)
                                .recipientId(1)
                                .pageNumber(1)
                                .xPosition(50)
                                .yPosition(750)
                                .type(DSTabType.TEXT)
                                .value(client.name)
                                .optional(true)
                                .build()                        // Name of the applicant
                        )
                        .build()
                )
                .recipient(
                    DSEnvelopeRecipient.Builder()
                        .recipientId(2)
                        .routingOrder(2)
                        .signerName("Jack Doe") // if someone needs a signed copy, their name here
                        .signerEmail("j.d@gmail.com") // if someone needs a signed copy, their email here
                        .type(DSRecipientType.CARBON_COPY)
                        .build()
                )
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

    fun buildEnvelopeDefaults(context: Context): DSEnvelopeDefaults {

        val sharedPreferences = context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val defaultClientDetails = Client(
            "FA-45231-005", "Tom Wood", "415-555-1234", "tom.wood@digital.com",
            "726 Tennessee St", "San Francisco", "CA 9107", "$25,000"
        )
        val defaultClientDetailsJson = Gson().toJson(defaultClientDetails)
        val clientString = sharedPreferences.getString(Constants.CLIENT_DETAILS_PREF, defaultClientDetailsJson)
        val client = Gson().fromJson<Client>(clientString, Client::class.java)

        val recipientDefaults = ArrayList<DSRecipientDefault>()
        val authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate()
        val user = authenticationDelegate.getLoggedInUser(context)


        val emailSubject = "Please sign TGK investment agreement"
        val emailBlurb = ""
        val envelopeTitle = "Investment Agreement"

        // In person signer
        val recipient1 = DSRecipientDefault(user.email,
        user.name,
        client.name,
        client.email,
        null,
        "IPS1",  // This should match the ROLE NAME in the template in DocuSign web portal
        DSEnvelopeDefaultsUniqueRecipientSelectorType.ROLE_NAME)

        // Receives carbon copy
        val recipient2 = DSRecipientDefault("j.d@gmail.com",
            "Jack Doe",
            null,
            null,
            null,
            "CC1",  // This should match the ROLE NAME in the template in DocuSign web portal
            DSEnvelopeDefaultsUniqueRecipientSelectorType.ROLE_NAME)

        recipientDefaults.add(recipient1)
        recipientDefaults.add(recipient2)

        val tabValueDefaults = hashMapOf<String, String>()
        // 'Text 6ffeb574-5e0f-4aa0-a2f6-7c402db4045e' is retrieved from 'Data Label' attribute in template document in DocuSign web portal
        tabValueDefaults["Text 6ffeb574-5e0f-4aa0-a2f6-7c402db4045e"] = client.addressLine1 // Address line 1
        tabValueDefaults["Text d470aabe-ae2f-468a-9d56-31e860e6fce0"] = client.addressLine2 // Address line 2
        tabValueDefaults["Text 9c42a785-e816-41de-82f5-6f1dabc56b37"] = client.addressLine3 // Address line 3
        tabValueDefaults["Text 2aa85b6b-5ff9-4c08-b78d-7d700595bbe0"] = client.name // Full name
        tabValueDefaults["Text fee82e45-3979-4a6d-b9d8-5da0b93f3546"] = client.id // Client number
        tabValueDefaults["Text 30870e40-0011-4360-99c3-26aa72fc65cd"] = client.investmentAmount // Investment amount
        tabValueDefaults["Text 0a22b39e-e3e7-455c-8683-d54f5d12ed71"] = client.name // Name of applicant

        val textCustomFields = getTextCustomFields(client)
        val listCustomFields: MutableList<DSListCustomField> = mutableListOf()
        val customFields = DSCustomFields(listCustomFields, textCustomFields as MutableList<DSTextCustomField>)

        return DSEnvelopeDefaults(
            recipientDefaults,
            emailSubject,
            emailBlurb,
            envelopeTitle,
            tabValueDefaults,
            customFields
        )
    }
}
