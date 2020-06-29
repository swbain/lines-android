package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stephenbain.lines.R

class HomeLoadStateViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_loading, parent, false)
    )

class HomeLoadStateAdapter : LoadStateAdapter<HomeLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: HomeLoadStateViewHolder, loadState: LoadState) {
        // do nothing
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): HomeLoadStateViewHolder {
        return HomeLoadStateViewHolder(parent)
    }

}