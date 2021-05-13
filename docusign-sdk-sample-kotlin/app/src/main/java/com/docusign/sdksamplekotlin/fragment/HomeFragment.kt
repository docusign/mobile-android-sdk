package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.adapter.HomePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class HomeFragment : Fragment() {

    companion object {
        val TAG = HomeFragment::class.java.simpleName

        fun newInstance(): HomeFragment {
            val bundle = Bundle().apply {
            }
            val fragment = HomeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val homePagerAdapter =
                HomePagerAdapter(
                    it,
                    it.supportFragmentManager
                )
            viewPager = it.findViewById(R.id.view_pager)
            viewPager.adapter = homePagerAdapter
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
