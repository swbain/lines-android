package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stephenbain.lines.R
import com.stephenbain.lines.databinding.ListItemTopicBinding

class HomeUiModelAdapter(private val picasso: Picasso) :
    PagingDataAdapter<TopicCardUiModel, TopicViewHolder>(HomeUiModelComparator) {
    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val item = getItem(position)
        item?.let(holder::bind)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = ListItemTopicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopicViewHolder(binding, picasso)
    }

    override fun getItemViewType(position: Int): Int = R.layout.list_item_topic
}

object HomeUiModelComparator : DiffUtil.ItemCallback<TopicCardUiModel>() {
    override fun areItemsTheSame(oldItem: TopicCardUiModel, newItem: TopicCardUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TopicCardUiModel,
        newItem: TopicCardUiModel
    ) = oldItem == newItem
}

class TopicViewHolder(binding: ListItemTopicBinding, picasso: Picasso) :
    RecyclerView.ViewHolder(binding.root) {

    private val view = TopicListItemView(binding, picasso)

    fun bind(item: TopicCardUiModel) {
        view.setItem(item)
    }
}