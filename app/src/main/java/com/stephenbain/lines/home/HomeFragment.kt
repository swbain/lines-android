package com.stephenbain.lines.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.stephenbain.lines.R
import com.stephenbain.lines.api.Topic
import com.stephenbain.lines.common.Resource
import com.stephenbain.lines.databinding.FragmentHomeBinding
import com.stephenbain.lines.databinding.ListItemDividerBinding
import com.stephenbain.lines.databinding.ListItemTopicBinding
import dagger.hilt.android.AndroidEntryPoint
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

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest(adapter::submitData)
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                Timber.d("load state = %s", it.refresh::class.simpleName)
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
}
