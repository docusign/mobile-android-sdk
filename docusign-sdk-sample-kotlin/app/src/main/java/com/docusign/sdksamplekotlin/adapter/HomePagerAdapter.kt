package com.docusign.sdksamplekotlin.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.fragment.OverviewFragment
import com.docusign.sdksamplekotlin.fragment.PendingSyncFragment
import com.docusign.sdksamplekotlin.fragment.TemplatesFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_overview,
    R.string.tab_templates,
    R.string.tab_pending_sync
)

class HomePagerAdapter(private val context: Context, fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment.newInstance()
            1 -> TemplatesFragment.newInstance()
            else -> PendingSyncFragment.newInstance()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}
