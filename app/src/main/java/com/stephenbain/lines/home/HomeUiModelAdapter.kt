package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.stephenbain.lines.R
import com.stephenbain.lines.databinding.ListItemDividerBinding
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
            R.layout.list_item_topic -> HomeViewHolder.TopicViewHolder(
                ListItemTopicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> HomeViewHolder.SeparatorViewHolder(
                ListItemDividerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
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

        val isSameSeparatorItem = oldItem is HomeItemUiModel.Separator
                && newItem is HomeItemUiModel.Separator

        return isSameTopicItem || isSameSeparatorItem
    }

    override fun areContentsTheSame(
        oldItem: HomeItemUiModel,
        newItem: HomeItemUiModel
    ) = oldItem == newItem

}

sealed class HomeViewHolder(viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    class TopicViewHolder(private val binding: ListItemTopicBinding) : HomeViewHolder(binding) {
        fun bind(item: HomeItemUiModel.TopicItem) {
            binding.title.text = item.topic.title
        }
    }

    class SeparatorViewHolder(binding: ListItemDividerBinding) : HomeViewHolder(binding)
}