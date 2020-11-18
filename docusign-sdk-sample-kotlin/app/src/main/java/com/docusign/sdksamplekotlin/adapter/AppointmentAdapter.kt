package com.docusign.sdksamplekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.model.Appointment

class AppointmentAdapter(var appointments: List<Appointment>, var listener: AppointmentListener) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    interface AppointmentListener {
        fun onAppointmentSelected(appointment: Appointment)
    }

    override fun getItemCount() = appointments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        return AppointmentViewHolder(LayoutInflater.from((parent.context)), parent)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    inner class AppointmentViewHolder(inflater: LayoutInflater, private val parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.item_appointment, parent, false)) {
        private var dateTextView = itemView.findViewById<TextView>(R.id.date_text_view)
        private var clientNameTextView = itemView.findViewById<TextView>(R.id.client_name_text_view)
        private var clientStatus = itemView.findViewById<TextView>(R.id.client_status_text_view)

        fun bind(appointment: Appointment) {
            itemView.setOnClickListener {
                listener.onAppointmentSelected(appointment)
            }

            dateTextView.text = appointment.date
            if (appointment.clientSigned) {
                clientStatus.text = itemView.context.getString(R.string.signed)
                clientStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark))
            } else  {
                clientStatus.text = itemView.context.getString(R.string.unsigned)
                clientStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
            }
            clientNameTextView.text = appointment.client.name
        }
    }
}
