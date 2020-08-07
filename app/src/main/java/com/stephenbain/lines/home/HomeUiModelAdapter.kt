package com.stephenbain.lines.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stephenbain.lines.R
import com.stephenbain.lines.api.avatarUrl
import com.stephenbain.lines.common.CircularTransformation
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

    class TopicViewHolder(parent: ViewGroup, private val picasso: Picasso) :
        HomeViewHolder(R.layout.list_item_topic, parent) {

        private val binding = ListItemTopicBinding.bind(itemView)

        private val imageViews = listOf(
            binding.imageView,
            binding.imageView2,
            binding.imageView3,
            binding.imageView4,
            binding.imageView5
        )

        fun bind(item: HomeItemUiModel.TopicItem) {
            binding.title.text = item.topic.title

            imageViews.forEachIndexed { index, imageView ->
                if (index < item.topic.users.size) {
                    imageView.doOnLayout {
                        picasso.load(item.topic.users[index].avatarUrl(it.measuredWidth))
                            .transform(CircularTransformation())
                            .into(imageView)
                    }
                    imageView.isVisible = true
                } else {
                    imageView.isVisible = false
                }
            }
        }
    }

    companion object {
        private fun inflateLayout(@LayoutRes resId: Int, parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(resId, parent, false)
        }
    }
}