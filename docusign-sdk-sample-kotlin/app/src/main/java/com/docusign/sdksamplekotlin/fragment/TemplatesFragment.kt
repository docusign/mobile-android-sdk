package com.docusign.sdksamplekotlin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.docusign.androidsdk.dsmodels.DSTemplate
import com.docusign.androidsdk.dsmodels.DSTemplatesFilter
import com.docusign.sdksamplekotlin.R
import com.docusign.sdksamplekotlin.adapter.TemplateAdapter
import com.docusign.sdksamplekotlin.livedata.GetTemplatesModel
import com.docusign.sdksamplekotlin.livedata.RetrieveCachedTemplateModel
import com.docusign.sdksamplekotlin.livedata.CacheTemplateModel
import com.docusign.sdksamplekotlin.livedata.RemoveCachedTemplateModel
import com.docusign.sdksamplekotlin.livedata.Status
import com.docusign.sdksamplekotlin.viewmodel.TemplatesViewModel

open class TemplatesFragment : Fragment(), TemplateAdapter.TemplateClickListener {

    companion object {
        val TAG = TemplatesFragment::class.java.simpleName
        const val TEMPLATE_COUNT = 50

        fun newInstance(): TemplatesFragment {
            val bundle = Bundle().apply {
            }
            val fragment = TemplatesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var startPosition: Int = 0
    private var availableTemplatesTotalSize = 0
    private var templatesTotalSize: Int = Integer.MAX_VALUE

    private lateinit var adapter: TemplateAdapter
    private lateinit var templates: ArrayList<DSTemplate>
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoadingData = false
    private lateinit var templatesViewModel: TemplatesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_templates, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            recyclerView = it.findViewById(R.id.templates_recycler_view)
            layoutManager = LinearLayoutManager(it)
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
            templatesViewModel = TemplatesViewModel()
            templates = ArrayList()
            adapter = TemplateAdapter(this)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            availableTemplatesTotalSize = 0
            startPosition = 0
            setRecyclerViewScrollListener()
            val filter = DSTemplatesFilter(TEMPLATE_COUNT, null, null, startPosition)
            isLoadingData = true
            initLiveDataObservers()
            templatesViewModel.getTemplates(requireContext(), filter)
        }
    }

    override fun downloadTemplate(templateId: String, position: Int) {
        templatesViewModel.cacheTemplate(templateId, position)
    }

    override fun removeDownloadedTemplate(templateId: String, position: Int) {
        templatesViewModel.removeCachedTemplate(templateId, position)
    }

    override fun retrieveDownloadedTemplate(templateId: String, position: Int) {
        templatesViewModel.retrieveCachedTemplate(templateId, position)
    }

    override fun templateSelected(templateId: String, templateName: String?) {
        /* NO-OP */
    }

    private fun setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoadingData && lastVisibleItemPosition == availableTemplatesTotalSize - 1) {
                    if (availableTemplatesTotalSize < templatesTotalSize) {
                        startPosition = availableTemplatesTotalSize
                        val filter = DSTemplatesFilter(TEMPLATE_COUNT, null, null, startPosition)
                        isLoadingData = true
                        templatesViewModel.getTemplates(requireContext(), filter)
                    }
                }
            }
        })
    }

    private fun toggleProgressBar(isBusy: Boolean) {
        activity?.findViewById<ProgressBar>(R.id.templates_progress_bar)?.visibility = if (isBusy) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun initLiveDataObservers() {
        templatesViewModel.getTemplatesLiveData.observe(viewLifecycleOwner, Observer<GetTemplatesModel> { model ->
            when (model.status) {
                Status.START -> {
                    toggleProgressBar(true)
                }
                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    model.dsTemplates?.let {
                        templatesTotalSize = model.dsTemplates.totalTemplatesSize
                        availableTemplatesTotalSize += model.dsTemplates.resultTemplatesSize
                        isLoadingData = false
                        adapter.addItems(model.dsTemplates.templates)
                    }
                }
                Status.ERROR -> {
                    toggleProgressBar(false)
                    templatesTotalSize = 0
                    isLoadingData = false
                    model.exception?.let { exception ->
                        Log.d(TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        templatesViewModel.cacheTemplateLiveData.observe(viewLifecycleOwner, Observer<CacheTemplateModel> { model ->
            when (model.status) {
                Status.START -> {
                    toggleProgressBar(true)
                }
                Status.COMPLETE -> {
                    toggleProgressBar(false)
                    model.template?.let {
                        adapter.updateItem(model.position, model.template.templateId, true)
                    }
                }
                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        templatesViewModel.removeCachedTemplateLiveData.observe(viewLifecycleOwner, Observer<RemoveCachedTemplateModel> { model ->
            when (model.status) {
                Status.START -> {
                    toggleProgressBar(true)
                }
                Status.COMPLETE -> {
                    model.templateDefinition?.let { templateDefinition ->
                        adapter.updateItem(model.position, templateDefinition.templateId, false)
                    }
                }
                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        templatesViewModel.retrieveCachedTemplateLiveData.observe(viewLifecycleOwner, Observer<RetrieveCachedTemplateModel> { model ->
            when (model.status) {
                Status.START -> {
                    /* NO-OP */
                }
                Status.COMPLETE -> {
                    model.templateDefinition?.let { templateDefinition ->
                        toggleProgressBar(false)
                        adapter.updateItem(model.position, templateDefinition.templateId, true)
                    }
                }
                Status.ERROR -> {
                    toggleProgressBar(false)
                    model.exception?.let { exception ->
                        Log.d(TAG, exception.message!!)
                        Toast.makeText(activity, model.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}
