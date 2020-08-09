package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stephenbain.lines.R
import com.stephenbain.lines.databinding.ListItemTopicBinding

class HomeUiModelAdapter(private val picasso: Picasso) :
    PagingDataAdapter<HomeItemUiModel, HomeViewHolder>(HomeUiModelComparator) {
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is HomeViewHolder.TopicViewHolder) {
            holder.bind(item as HomeItemUiModel.TopicItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.TopicViewHolder(parent, picasso)
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HomeItemUiModel.TopicItem -> R.layout.list_item_topic
        null -> throw IllegalStateException("Unknown view")
    }
}

object HomeUiModelComparator : DiffUtil.ItemCallback<HomeItemUiModel>() {
    override fun areItemsTheSame(oldItem: HomeItemUiModel, newItem: HomeItemUiModel): Boolean {
        return oldItem is HomeItemUiModel.TopicItem
                && newItem is HomeItemUiModel.TopicItem
                && oldItem.topic.id == newItem.topic.id
    }

    override fun areContentsTheSame(
        oldItem: HomeItemUiModel,
        newItem: HomeItemUiModel
    ) = oldItem == newItem
}

sealed class HomeViewHolder(@LayoutRes resId: Int, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflateLayout(resId, parent)) {

    class TopicViewHolder(parent: ViewGroup, picasso: Picasso) :
        HomeViewHolder(R.layout.list_item_topic, parent) {

        private val view = TopicListItemView(ListItemTopicBinding.bind(itemView), picasso)

        fun bind(item: HomeItemUiModel.TopicItem) {
            view.setItem(item)
        }
    }

    companion object {
        private fun inflateLayout(@LayoutRes resId: Int, parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(resId, parent, false)
        }
    }
}