package com.docusign.sdksamplekotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.exceptions.DSAuthenticationException
import com.docusign.androidsdk.listeners.DSLogoutListener
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.fragment.HomeFragment
import com.docusign.sdksamplekotlin.utils.ClientUtils
import com.docusign.sdksamplekotlin.utils.Constants

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG)
        if (homeFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            homeFragment = HomeFragment.newInstance()
            fragmentTransaction.add(R.id.home_container, homeFragment, HomeFragment.TAG)
            fragmentTransaction.commit()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> logout()
            R.id.action_reset -> reset()
        }
        return true
    }

    private fun logout() {
        // DS: Logout from DocuSign
        DocuSign.getInstance().getAuthenticationDelegate().logout(this, true, object : DSLogoutListener {

            override fun onSuccess() {
                finish()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            override fun onError(exception: DSAuthenticationException) {
               Toast.makeText(applicationContext, "Failed to logout: " + exception.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun reset() {
        ClientUtils.setSignedStatus(applicationContext, Constants.CLIENT_A_PREF, false)
        ClientUtils.setSignedStatus(applicationContext, Constants.CLIENT_B_PREF, false)
        val homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG)
        (homeFragment as? HomeFragment)?.viewPager?.adapter?.notifyDataSetChanged()
    }
}
