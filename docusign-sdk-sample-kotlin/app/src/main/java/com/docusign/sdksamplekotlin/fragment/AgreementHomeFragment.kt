package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.adapter.AgreementHomePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class AgreementHomeFragment : Fragment() {

    companion object {
        val TAG = AgreementHomeFragment::class.java.simpleName

        fun newInstance(): AgreementHomeFragment {
            val bundle = Bundle().apply {
            }
            val fragment = AgreementHomeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_agreement_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val agreementHomePagerAdapter =
                AgreementHomePagerAdapter(
                    it,
                    it.supportFragmentManager
                )
            viewPager = it.findViewById(R.id.view_pager)
            viewPager.adapter = agreementHomePagerAdapter
            val tabLayout: TabLayout = it.findViewById(R.id.tabs)
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.addOnTabSelectedListener(onTabSelectedListener)
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager.currentItem = tab.position
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    }
}
