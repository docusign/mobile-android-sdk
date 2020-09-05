package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.adapter.AppointmentAdapter
import com.docusign.sdksamplekotlin.model.Appointment

class OverviewFragment : Fragment() {
    companion object {
        val TAG = OverviewFragment::class.java.simpleName

        fun newInstance(): OverviewFragment {
            val bundle = Bundle().apply {
            }
            val fragment = OverviewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            val appointmentRecyclerView = it.findViewById<RecyclerView>(R.id.appointments_recycler_view)
            appointmentRecyclerView.layoutManager = LinearLayoutManager(it)
            val appointments = mutableListOf<Appointment>()
            appointments.add(Appointment("Sep 2, 2020", "Tom Wood", false))
            appointments.add(Appointment("Sep 1, 2020", "Andrea G Kuhn", true))
            appointmentRecyclerView.adapter = AppointmentAdapter(appointments)
        }
    }
}