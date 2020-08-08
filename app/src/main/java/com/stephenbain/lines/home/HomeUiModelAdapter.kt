package com.stephenbain.lines.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stephenbain.lines.R
import com.stephenbain.lines.api.User
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
            if (item.topic.category != null) {
                binding.category.text = item.topic.category.name
                val bgColor = Color.parseColor( "#${item.topic.category.color}")
                binding.categoryColor.setBackgroundColor(bgColor)

                binding.category.isVisible = true
                binding.categoryColor.isVisible = true
            } else {
                binding.category.isVisible = false
                binding.categoryColor.isVisible = false
            }

            imageViews.forEachIndexed { index, imageView ->
                if (index < item.topic.users.size) {
                    loadImage(imageView, item.topic.users[index])
                    imageView.isVisible = true
                } else {
                    imageView.isVisible = false
                }
            }
        }

        private fun loadImage(imageView: ImageView, user: User) {
            if (imageView.measuredWidth == 0) {
                imageView.doOnLayout {
                    loadImage(imageView, user, imageView.measuredWidth)
                }
            } else loadImage(imageView, user, imageView.measuredWidth)
        }

        private fun loadImage(imageView: ImageView, user: User, size: Int) {
            picasso.load(user.avatarUrl(size))
                .transform(CircularTransformation())
                .tag(user)
                .into(imageView)
        }
    }

    companion object {
        private fun inflateLayout(@LayoutRes resId: Int, parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(resId, parent, false)
        }
    }
}