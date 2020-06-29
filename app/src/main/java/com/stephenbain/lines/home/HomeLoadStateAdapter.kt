package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stephenbain.lines.R
import com.stephenbain.lines.databinding.ListItemLoadingBinding

class HomeLoadStateViewHolder(parent: ViewGroup, retry: () -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_loading, parent, false)
    ) {

    private val binding = ListItemLoadingBinding.bind(itemView)
    private val loading = binding.loading
    private val retry = binding.retry.apply { setOnClickListener { retry() } }

    fun bind(loadState: LoadState) {
        loading.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
    }
}

class HomeLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<HomeLoadStateViewHolder>() {



    override fun onBindViewHolder(holder: HomeLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): HomeLoadStateViewHolder {
        return HomeLoadStateViewHolder(parent, retry)
    }

}