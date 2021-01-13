package com.docusign.sdksamplekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.docusign.androidsdk.dsmodels.DSTemplate
import com.docusign.sdksamplekotlin.R

class TemplateAdapter(private val templateClickListener: TemplateClickListener) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    interface TemplateClickListener {
        fun downloadTemplate(templateId: String, position: Int)
        fun retrieveDownloadedTemplate(templateId: String, position: Int)
        fun removeDownloadedTemplate(templateId: String, position: Int)
        fun templateSelected(templateId: String, templateName: String?)
    }

    private val templates = mutableListOf<DSTemplate>()
    private val cacheTemplateMap = mutableMapOf<String, Boolean>()

    override fun getItemCount() = templates.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        return TemplateViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        holder.bind(templates[position], position)
    }

    fun addItems(templateList: List<DSTemplate>) {
        templates.addAll(templateList)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, templateId: String, cached: Boolean) {
        cacheTemplateMap[templateId] = cached
        notifyItemChanged(position)
    }

    inner class TemplateViewHolder(inflater: LayoutInflater, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.item_template, parent, false)
        ) {
        private var templateNameTextView = itemView.findViewById<TextView>(R.id.template_name_text_view)
        private var templateDownloadButton = itemView.findViewById<Button>(R.id.template_download_button)
        private var deleteTemplate = false

        fun bind(template: DSTemplate, position: Int) {
            templateNameTextView.text = template.templateName
            itemView.setOnClickListener {
                deleteTemplate = false
                if (cacheTemplateMap[template.templateId] == true) {
                    templateDownloadButton.setBackgroundResource(R.drawable.download_done)
                } else {
                    templateDownloadButton.setBackgroundResource(R.drawable.download)
                }
                templateClickListener.templateSelected(template.templateId, template.templateName)
            }
            if (cacheTemplateMap[template.templateId] == true) {
                templateDownloadButton.setBackgroundResource(R.drawable.download_done)
            } else {
                templateDownloadButton.setBackgroundResource(R.drawable.download)
                templateClickListener.retrieveDownloadedTemplate(template.templateId, position)
            }
            templateDownloadButton.setOnClickListener {
                if (cacheTemplateMap[template.templateId] != true) {
                    deleteTemplate = false
                    templateClickListener.downloadTemplate(template.templateId, position)
                } else {
                    if (deleteTemplate) {
                        templateClickListener.removeDownloadedTemplate(template.templateId, position)
                        deleteTemplate = false
                    } else {
                        templateDownloadButton.setBackgroundResource(R.drawable.ic_delete)
                        deleteTemplate = true
                    }
                }
            }
        }
    }
}
