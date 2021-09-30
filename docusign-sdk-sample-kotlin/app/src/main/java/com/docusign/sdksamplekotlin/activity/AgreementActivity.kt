package com.docusign.sdksamplekotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.fragment.AgreementHomeFragment
import com.docusign.sdksamplekotlin.fragment.AgreementTemplatesFragment
import com.docusign.sdksamplekotlin.fragment.ClientInvestmentFragment
import com.docusign.sdksamplekotlin.fragment.PortfolioFragment
import com.docusign.sdksamplekotlin.model.Client
import com.google.gson.Gson

class AgreementActivity : AppCompatActivity(), PortfolioFragment.PortFolioFragmentListener,
    ClientInvestmentFragment.ClientInvestmentFragmentListener {

    private var client: Client? = null

    companion object {
        private val TAG = AgreementActivity::class.java.simpleName
        const val CLIENT_DETAILS = "ClientDetails"
        const val OPEN_CLIENT_INVESTMENT = "Open client investment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clientJson = intent?.getStringExtra(CLIENT_DETAILS)
        clientJson?.let {
            client = Gson().fromJson(clientJson, Client::class.java)
        }
        setContentView(R.layout.activity_agreement)
        var agreementHomeFragment =
            supportFragmentManager.findFragmentByTag(AgreementHomeFragment.TAG)
        if (agreementHomeFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            agreementHomeFragment = AgreementHomeFragment.newInstance(client)
            fragmentTransaction.add(
                R.id.agreement_container,
                agreementHomeFragment,
                AgreementHomeFragment.TAG
            )
            fragmentTransaction.addToBackStack(AgreementHomeFragment.TAG)
            fragmentTransaction.commit()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        intent?.let {
            if (it.getBooleanExtra(OPEN_CLIENT_INVESTMENT, false)) {
                displayClientInvestment(client)
            }
        }
    }

    override fun displayClientInvestment(client: Client?) {
        var clientInvestmentFragment =
            supportFragmentManager.findFragmentByTag(ClientInvestmentFragment.TAG)
        if (clientInvestmentFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            clientInvestmentFragment = ClientInvestmentFragment.newInstance(client)
            fragmentTransaction.add(
                R.id.agreement_container,
                clientInvestmentFragment,
                ClientInvestmentFragment.TAG
            )
            fragmentTransaction.addToBackStack(ClientInvestmentFragment.TAG)
            fragmentTransaction.commit()
        }
    }

    override fun displayAgreementTemplates(client: Client?) {
        var agreementTemplatesFragment =
            supportFragmentManager.findFragmentByTag(AgreementTemplatesFragment.TAG)
        if (agreementTemplatesFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            agreementTemplatesFragment = AgreementTemplatesFragment.newInstance(client)
            fragmentTransaction.add(
                R.id.agreement_container,
                agreementTemplatesFragment,
                AgreementTemplatesFragment.TAG
            )
            fragmentTransaction.addToBackStack(AgreementTemplatesFragment.TAG)
            fragmentTransaction.commit()
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }
}
