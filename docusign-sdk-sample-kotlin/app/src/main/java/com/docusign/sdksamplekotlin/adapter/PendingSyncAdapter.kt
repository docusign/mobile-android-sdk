package com.docusign.sdksamplekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.docusign.androidsdk.dsmodels.DSEnvelope
import com.docusign.sdksamplekotlin.R

class PendingSyncAdapter(private val pendingSyncListener: PendingSyncListener) : RecyclerView.Adapter<PendingSyncAdapter.PendingSyncViewHolder>() {

    private val envelopes = mutableListOf<DSEnvelope>()

    interface PendingSyncListener {
        fun syncEnvelope(position: Int, envelopeId: String)
    }

    override fun getItemCount() = envelopes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingSyncViewHolder {
        return PendingSyncViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: PendingSyncViewHolder, position: Int) {
        holder.bind(envelopes[position], position)
    }

    fun addItem(envelope: DSEnvelope) {
        envelopes.add(envelope)
        notifyDataSetChanged()
    }

    fun addItems(envelopesList: List<DSEnvelope>) {
        envelopes.addAll(envelopesList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        envelopes.removeAt(position)
        notifyItemChanged(position)
    }

    fun removeAll() {
        envelopes.clear()
        notifyDataSetChanged()
    }

    fun getSize(): Int =  envelopes.size

    inner class PendingSyncViewHolder(inflater: LayoutInflater, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.item_pending_sync, parent, false)
        ) {
        private var envelopeNameTextView = itemView.findViewById<TextView>(R.id.envelope_name_text_view)
        private var envelopeSyncButton = itemView.findViewById<Button>(R.id.envelope_sync_button)

        fun bind(envelope: DSEnvelope, position: Int) {
            val names = envelope.emailSubject?.split("Please DocuSign: ")
            if (!names.isNullOrEmpty()) {
                envelopeNameTextView.text = names[names.size - 1]
            }

            envelopeSyncButton.setOnClickListener {
                pendingSyncListener.syncEnvelope(position, envelope.envelopeId)
            }
        }
    }
}
