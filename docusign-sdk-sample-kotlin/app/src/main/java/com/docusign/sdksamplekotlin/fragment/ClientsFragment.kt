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
            clients.add(Client("1", "Tom Wood", "111-1111111", "t.w@gmail.com", "120 Geary Street, San Francisco"))
            clients.add(Client("2", "Andrea G Kuhn", "122-1111122", "a.k@gmail.com", "120 Downing Street, New York"))
            clientsRecyclerView.adapter = ClientAdapter(clients)
        }
    }
}