package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stephenbain.lines.R
import com.stephenbain.lines.databinding.ListItemTopicBinding

class HomeUiModelAdapter :
    PagingDataAdapter<HomeItemUiModel, HomeViewHolder>(HomeUiModelComparator) {
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is HomeViewHolder.TopicViewHolder) {
            holder.bind(item as HomeItemUiModel.TopicItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder =
        when (viewType) {
            R.layout.list_item_topic -> HomeViewHolder.TopicViewHolder(parent)
            else -> HomeViewHolder.SeparatorViewHolder(parent)
        }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HomeItemUiModel.TopicItem -> R.layout.list_item_topic
        HomeItemUiModel.Separator -> R.layout.list_item_divider
        null -> throw IllegalStateException("Unknown view")
    }

}

object HomeUiModelComparator : DiffUtil.ItemCallback<HomeItemUiModel>() {
    override fun areItemsTheSame(oldItem: HomeItemUiModel, newItem: HomeItemUiModel): Boolean {
        val isSameTopicItem = oldItem is HomeItemUiModel.TopicItem
                && newItem is HomeItemUiModel.TopicItem
                && oldItem.topic.id == newItem.topic.id
                && oldItem.category == newItem.category

        val isSameSeparatorItem = oldItem is HomeItemUiModel.Separator
                && newItem is HomeItemUiModel.Separator

        return isSameTopicItem || isSameSeparatorItem
    }

    override fun areContentsTheSame(
        oldItem: HomeItemUiModel,
        newItem: HomeItemUiModel
    ) = oldItem == newItem

}

sealed class HomeViewHolder(@LayoutRes resId: Int, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflateLayout(resId, parent)) {

    class TopicViewHolder(parent: ViewGroup) : HomeViewHolder(R.layout.list_item_topic, parent) {

        private val binding = ListItemTopicBinding.bind(itemView)

        fun bind(item: HomeItemUiModel.TopicItem) {
            binding.title.text = item.topic.title
        }
    }

    class SeparatorViewHolder(parent: ViewGroup) : HomeViewHolder(R.layout.list_item_divider, parent)

    companion object {
        private fun inflateLayout(@LayoutRes resId: Int, parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(resId, parent, false)
        }
    }
}