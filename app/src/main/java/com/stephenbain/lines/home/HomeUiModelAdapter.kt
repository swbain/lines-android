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

    override fun onBindViewHolder(
        holder: TopicViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.filterIsInstance<ChangePayload>().forEach { payload ->
                payload.imageTemplates?.let(holder::updateImages)
                payload.subtitle?.let(holder::updateSubtitle)
            }
        } else super.onBindViewHolder(holder, position, payloads)
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

    override fun getChangePayload(oldItem: TopicCardUiModel, newItem: TopicCardUiModel): Any? {
        val imagesChanged = oldItem.userImageUrlTemplates != newItem.userImageUrlTemplates
        val subtitleChanged = oldItem.subtitle != newItem.subtitle

        return if (imagesChanged || subtitleChanged) ChangePayload(
            imageTemplates = if (imagesChanged) newItem.userImageUrlTemplates else null,
            subtitle = if (subtitleChanged) newItem.subtitle else null
        ) else null
    }
}

data class ChangePayload(val imageTemplates: List<String>?, val subtitle: String?)

class TopicViewHolder(binding: ListItemTopicBinding, picasso: Picasso) :
    RecyclerView.ViewHolder(binding.root) {

    private val view = TopicListItemView(binding, picasso)

    fun bind(item: TopicCardUiModel) {
        view.setItem(item)
    }

    fun updateImages(imageTemplates: List<String>) {
        view.loadImages(imageTemplates)
    }

    fun updateSubtitle(subtitle: String) {
        view.updateSubtitle(subtitle)
    }
}