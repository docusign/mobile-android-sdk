package com.docusign.sdksamplekotlin.adapter

import com.docusign.sdksamplekotlin.fragment.PortfolioFragment
import com.docusign.sdksamplekotlin.fragment.ContactFragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.model.Client

private val TAB_TITLES = arrayOf(
    R.string.tab_portfolio,
    R.string.tab_contact
)

class AgreementHomePagerAdapter(private val client: Client?, private val context: Context, fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PortfolioFragment.newInstance(client)
            else -> ContactFragment.newInstance(client)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}
