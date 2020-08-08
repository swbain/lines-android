package com.stephenbain.lines.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.view.doOnLayout
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
import java.math.RoundingMode
import java.text.DecimalFormat

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

            val replies = itemView.context.resources.getQuantityString(
                R.plurals.replies_label,
                item.topic.postCount - 1,
                (item.topic.postCount - 1).toFormattedCount()
            )
            val views = itemView.context.resources.getQuantityString(
                R.plurals.views_label,
                item.topic.views,
                item.topic.views.toFormattedCount()
            )

            binding.subtitle.text = itemView.context.resources.getString(
                R.string.topic_card_subtitle,
                replies, views
            )

            if (item.topic.category != null) {
                binding.category.text = item.topic.category.name
                val bgColor = Color.parseColor( "#${item.topic.category.color}")
                binding.categoryColor.setBackgroundColor(bgColor)

                binding.categoryGroup.isVisible = true
            } else {
                binding.categoryGroup.isVisible = false
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

        private fun Int.toFormattedCount(): String {
            return when {
                this < 1000 -> toString()
                this % 1000 == 0 -> "${this / 1000}k"
                else -> {
                    val df = DecimalFormat("#.#")
                    df.roundingMode = RoundingMode.HALF_EVEN
                    "${df.format(toFloat() / 1000F)}k".replace(".0", "")
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