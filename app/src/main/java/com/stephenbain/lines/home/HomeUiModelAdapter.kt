package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stephenbain.lines.databinding.ListItemTopicBinding
import com.stephenbain.lines.databinding.ListSeparatorTopicBinding

class HomeUiModelAdapter(private val picasso: Picasso) :
    PagingDataAdapter<TopicListItemUiModel, TopicsListViewHolder<*>>(HomeUiModelComparator) {
    override fun onBindViewHolder(holder: TopicsListViewHolder<*>, position: Int) {
        when (val item = getItem(position)) {
            is TopicListItemUiModel.TopicCard -> (holder as? TopicsListViewHolder.CardViewHolder)?.bind(item)
            is TopicListItemUiModel.Separator -> (holder as? TopicsListViewHolder.SeparatorViewHolder)?.bind(item)
        }
    }

    override fun onBindViewHolder(
        holder: TopicsListViewHolder<*>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.filterIsInstance<ChangePayload>().forEach { payload ->
                if (holder is TopicsListViewHolder.CardViewHolder) {
                    payload.imageTemplates?.let(holder::updateImages)
                    payload.subtitle?.let(holder::updateSubtitle)
                }
            }
        } else super.onBindViewHolder(holder, position, payloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsListViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_TOPIC -> {
                val binding = ListItemTopicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TopicsListViewHolder.CardViewHolder(binding, picasso)
            }
            VIEW_TYPE_SEPARATOR -> {
                TopicsListViewHolder.SeparatorViewHolder(
                    ListSeparatorTopicBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalStateException("invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TopicListItemUiModel.TopicCard -> VIEW_TYPE_TOPIC
            is TopicListItemUiModel.Separator -> VIEW_TYPE_SEPARATOR
            null -> throw IllegalStateException("item should never be null")
        }
    }

    companion object {
        private const val VIEW_TYPE_TOPIC = 0
        private const val VIEW_TYPE_SEPARATOR = 1
    }
}

object HomeUiModelComparator : DiffUtil.ItemCallback<TopicListItemUiModel>() {
    override fun areItemsTheSame(
        oldItem: TopicListItemUiModel,
        newItem: TopicListItemUiModel
    ): Boolean {
        return when {
            oldItem is TopicListItemUiModel.TopicCard && newItem is TopicListItemUiModel.TopicCard -> oldItem.id == newItem.id
            oldItem is TopicListItemUiModel.Separator && newItem is TopicListItemUiModel.Separator -> oldItem.text == newItem.text
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: TopicListItemUiModel,
        newItem: TopicListItemUiModel
    ) = oldItem == newItem

    override fun getChangePayload(
        oldItem: TopicListItemUiModel,
        newItem: TopicListItemUiModel
    ): Any? {
        return if (oldItem is TopicListItemUiModel.TopicCard && newItem is TopicListItemUiModel.TopicCard) {
            val imagesChanged = oldItem.userImageUrlTemplates != newItem.userImageUrlTemplates
            val subtitleChanged = oldItem.subtitle != newItem.subtitle

            if (imagesChanged || subtitleChanged) ChangePayload(
                imageTemplates = if (imagesChanged) newItem.userImageUrlTemplates else null,
                subtitle = if (subtitleChanged) newItem.subtitle else null
            ) else null
        } else null
    }
}

data class ChangePayload(val imageTemplates: List<String>?, val subtitle: String?)

sealed class TopicsListViewHolder<T : TopicListItemUiModel>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: T)

    class CardViewHolder(binding: ListItemTopicBinding, picasso: Picasso) :
        TopicsListViewHolder<TopicListItemUiModel.TopicCard>(binding.root) {

        private val view = TopicListItemView(binding, picasso)

        override fun bind(item: TopicListItemUiModel.TopicCard) {
            view.setItem(item)
        }

        fun updateImages(imageTemplates: List<String>) {
            view.loadImages(imageTemplates)
        }

        fun updateSubtitle(subtitle: String) {
            view.updateSubtitle(subtitle)
        }
    }

    class SeparatorViewHolder(private val binding: ListSeparatorTopicBinding) :
        TopicsListViewHolder<TopicListItemUiModel.Separator>(binding.root) {
        override fun bind(item: TopicListItemUiModel.Separator) {
            binding.text.text = item.text
        }

    }
}