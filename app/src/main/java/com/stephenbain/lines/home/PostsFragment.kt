package com.stephenbain.lines.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.stephenbain.lines.databinding.FragmentPostsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PostsFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    private val picasso = Picasso.get()

    private val adapter = HomeUiModelAdapter(picasso)

    private lateinit var binding: FragmentPostsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        setUpLoading()

        lifecycleScope.launch {
            viewModel.data.collectLatest { adapter.submitData(it) }
        }
    }

    private fun setUpRecycler() {
        binding.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = HomeLoadStateAdapter(adapter::retry),
            footer = HomeLoadStateAdapter(adapter::retry)
        )

        binding.retry.setOnClickListener {
            adapter.retry()
        }
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setUpLoading() = lifecycleScope.launchWhenCreated {
        adapter.loadStateFlow.collectLatest {
            val isRefreshing = it.refresh is LoadState.Loading
            val isError = it.refresh is LoadState.Error

            if (!isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }

            binding.retry.isVisible = isError
        }
    }
}
