package com.docusign.sdksamplekotlin.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.activity.AgreementActivity
import com.docusign.sdksamplekotlin.adapter.AppointmentAdapter
import com.docusign.sdksamplekotlin.model.Appointment
import com.docusign.sdksamplekotlin.model.Client
import com.docusign.sdksamplekotlin.utils.Constants
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OverviewFragment : Fragment(), AppointmentAdapter.AppointmentListener {

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
        activity?.let { activity ->
            val appointmentRecyclerView = activity.findViewById<RecyclerView>(R.id.appointments_recycler_view)
            appointmentRecyclerView.layoutManager = LinearLayoutManager(activity)
            val appointments = mutableListOf<Appointment>()
            val date = Date()
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

            val client1 = Client(
                "FA-45231-005",
                "Tom Wood",
                "415-555-1234",
                "tom.wood@digital.com",
                "726 Tennessee St",
                "San Francisco, CA",
                "USA - 94107",
                "$300,0000",
                Constants.CLIENT_A_PREF
            )
            val client2 = Client(
                "FA-45231-006",
                "Andrea G Kuhn",
                "415-555-1235",
                "andrea.kuhn@global.com",
                "231 Dalton Way",
                "New York, CA",
                "USA - 10005",
                "$500,0000",
                Constants.CLIENT_B_PREF
            )
            val client1Json = Gson().toJson(client1)
            val client2Json = Gson().toJson(client2)
            val sharedPreferences = context?.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            sharedPreferences?.edit()?.apply {
                putString(Constants.CLIENT_A_PREF, client1Json)?.apply()
                putString(Constants.CLIENT_B_PREF, client2Json)?.apply()
            }

            appointments.add(Appointment(dateFormat.format(date), client1, false))
            appointments.add(Appointment(dateFormat.format(date), client2, false))
            appointmentRecyclerView.adapter = AppointmentAdapter(appointments, this)
        }
    }

    override fun onAppointmentSelected(appointment: Appointment) {
        val intent = Intent(activity, AgreementActivity::class.java)
        val clientJson = Gson().toJson(appointment.client)
        clientJson?.let {
            intent.putExtra(AgreementActivity.CLIENT_DETAILS, clientJson)
        }
        activity?.startActivity(intent)
    }
}
