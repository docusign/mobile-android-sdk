package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.adapter.ClientAdapter
import com.docusign.sdksamplekotlin.model.Client

class ClientsFragment : Fragment() {

    companion object {
        val TAG = ClientsFragment::class.java.simpleName

        fun newInstance(): ClientsFragment {
            val bundle = Bundle().apply {
            }
            val fragment = ClientsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clients, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            val clientsRecyclerView = it.findViewById<RecyclerView>(R.id.clients_recycler_view)
            clientsRecyclerView.layoutManager = LinearLayoutManager(it)
            val clients = mutableListOf<Client>()
            val defaultClientDetails = Client(
                "FA-45231-005", "Tom Wood", "415-555-1234", "tom.wood@digital.com",
                "726 Tennessee St", "San Francisco", "CA 9107", "$25,000"
            )
            clients.add(
                Client(
                    "FA-45231-005",
                    "Tom Wood",
                    "415-555-1234",
                    "tom.wood@digital.com",
                    "726 Tennessee St",
                    "San Francisco",
                    "CA 9107",
                    "$25,000"
                )
            )
            clients.add(
                Client(
                    "FA-45231-006",
                    "Andrea G Kuhn",
                    "415-555-1235",
                    "andrea.kuhn@infotech.com",
                    "120 Downing St",
                    "New York",
                    "NY 92333",
                    "$10,000"
                )
            )
            clientsRecyclerView.adapter = ClientAdapter(clients)
        }
    }
}
