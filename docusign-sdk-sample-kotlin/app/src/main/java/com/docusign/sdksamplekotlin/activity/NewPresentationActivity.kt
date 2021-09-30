package com.docusign.sdksamplekotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.fragment.NewPresentationFragment

class NewPresentationActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_presentation)

        var newPresentationFragment =
            supportFragmentManager.findFragmentByTag(NewPresentationFragment.TAG)

        if(newPresentationFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            newPresentationFragment = NewPresentationFragment.newInstance()
            fragmentTransaction.add(
                R.id.new_presentation_container,
                newPresentationFragment,
                NewPresentationFragment.TAG
            )
            fragmentTransaction.addToBackStack(NewPresentationFragment.TAG)
            fragmentTransaction.commit()
        }

        supportActionBar?.let { actionBar ->
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setLogo(R.drawable.ic_logo)
            actionBar.setDisplayUseLogoEnabled(true)
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