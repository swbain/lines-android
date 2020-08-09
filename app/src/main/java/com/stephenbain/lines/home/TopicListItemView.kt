package com.stephenbain.lines.home

import android.graphics.Color
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import com.stephenbain.lines.R
import com.stephenbain.lines.api.User
import com.stephenbain.lines.api.avatarUrl
import com.stephenbain.lines.common.CircularTransformation
import com.stephenbain.lines.databinding.ListItemTopicBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class TopicListItemView(private val binding: ListItemTopicBinding, private val picasso: Picasso) {

    private val imageViews = listOf(
        binding.imageView,
        binding.imageView2,
        binding.imageView3,
        binding.imageView4,
        binding.imageView5
    )

    private val resources = binding.root.context.resources

    fun setItem(item: HomeItemUiModel.TopicItem) {
        binding.title.text = item.topic.title

        val replies = resources.getQuantityString(
            R.plurals.replies_label,
            item.topic.postCount - 1,
            (item.topic.postCount - 1).toFormattedCount()
        )
        val views = resources.getQuantityString(
            R.plurals.views_label,
            item.topic.views,
            item.topic.views.toFormattedCount()
        )

        binding.subtitle.text = resources.getString(
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