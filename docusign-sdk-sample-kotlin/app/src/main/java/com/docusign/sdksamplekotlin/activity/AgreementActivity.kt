package com.docusign.sdksamplekotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.fragment.*

class AgreementActivity : AppCompatActivity(), PortfolioFragment.PortFolioFragmentListener,
    ClientInvestmentFragment.ClientInvestmentFragmentListener {

    companion object {
        private val TAG = AgreementActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)
        var agreementHomeFragment = supportFragmentManager.findFragmentByTag(AgreementHomeFragment.TAG)
        if (agreementHomeFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            agreementHomeFragment = AgreementHomeFragment.newInstance()
            fragmentTransaction.add(R.id.agreement_container, agreementHomeFragment, AgreementHomeFragment.TAG)
            fragmentTransaction.addToBackStack(AgreementHomeFragment.TAG)
            fragmentTransaction.commit()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)
    }

    override fun displayClientInvestment() {
        var clientInvestmentFragment = supportFragmentManager.findFragmentByTag(ClientInvestmentFragment.TAG)
        if (clientInvestmentFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            clientInvestmentFragment = ClientInvestmentFragment.newInstance()
            fragmentTransaction.add(R.id.agreement_container, clientInvestmentFragment, ClientInvestmentFragment.TAG)
            fragmentTransaction.addToBackStack(ClientInvestmentFragment.TAG)
            fragmentTransaction.commit()
        }
    }

    override fun displayAgreementTemplates() {
        var agreementTemplatesFragment = supportFragmentManager.findFragmentByTag(AgreementTemplatesFragment.TAG)
        if (agreementTemplatesFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            agreementTemplatesFragment = AgreementTemplatesFragment.newInstance()
            fragmentTransaction.add(R.id.agreement_container, agreementTemplatesFragment, AgreementTemplatesFragment.TAG)
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
