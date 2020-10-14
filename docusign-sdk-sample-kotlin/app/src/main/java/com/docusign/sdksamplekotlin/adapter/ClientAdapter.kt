package com.docusign.sdksamplekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.model.Client

class ClientAdapter(var clients: List<Client>) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    override fun getItemCount() = clients.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        return ClientViewHolder(LayoutInflater.from((parent.context)), parent)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(clients[position])
    }

    class ClientViewHolder(inflater: LayoutInflater, private val parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.item_client, parent, false)) {
        private var clientTextView = itemView.findViewById<TextView>(R.id.client_text_view)

        fun bind(client: Client) {
            clientTextView.text = client.name
        }
    }
}
