package com.stephenbain.lines.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.stephenbain.lines.MainActivity
import com.stephenbain.lines.R
import com.stephenbain.lines.api.Category
import com.stephenbain.lines.api.Topic
import com.stephenbain.lines.common.Resource
import com.stephenbain.lines.common.exhaustive
import com.stephenbain.lines.databinding.*
import com.stephenbain.lines.repository.CategoryItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_loading.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = HomeUiModelAdapter()

    private lateinit var binding: FragmentHomeBinding

    private var initialLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        setUpLoading()
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
        viewModel.categories.observe(viewLifecycleOwner) { setUpToolbar(it)  }
    }

    private fun setUpToolbar(categories: List<CategoryItem>) {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_item_categories)
        val menuItem = toolbar.menu.findItem(R.id.spinner)
        val spinner = menuItem.actionView as Spinner
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            categories.map { categoryItem ->
                when (categoryItem) {
                    CategoryItem.AllCategories -> "All Categories"
                    is CategoryItem.SelectedCategory -> categoryItem.category.name
                }
            }
        )

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // no-op
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSelectedCategory(categories[position])
            }

        }
    }

    private fun setUpRecycler() {
        binding.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = HomeLoadStateAdapter(adapter::retry),
            footer = HomeLoadStateAdapter(adapter::retry)
        )

        binding.retry.setOnClickListener {
            initialLoad = true
            adapter.retry()
        }
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setUpLoading() = lifecycleScope.launchWhenCreated {
        adapter.loadStateFlow.collectLatest {
            val isLoading = it.refresh is LoadState.Loading
            val isError = it.refresh is LoadState.Error

            binding.loading.isVisible = isLoading && initialLoad
            if (!isLoading) {
                binding.swipeRefresh.isRefreshing = false
            }


            binding.retry.isVisible = isError
            binding.recycler.isVisible = (isLoading && !initialLoad) || it.refresh is LoadState.NotLoading

            if (initialLoad && isLoading) {
                initialLoad = false
            }
        }
    }
}
