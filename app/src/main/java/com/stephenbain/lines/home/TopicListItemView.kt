package com.stephenbain.lines.home

import android.graphics.Color
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import com.stephenbain.lines.api.BASE_URL
import com.stephenbain.lines.common.CircularTransformation
import com.stephenbain.lines.databinding.ListItemTopicBinding

class TopicListItemView(private val binding: ListItemTopicBinding, private val picasso: Picasso) {

    private val imageViews = listOf(
        binding.imageView,
        binding.imageView2,
        binding.imageView3,
        binding.imageView4,
        binding.imageView5
    )

    fun setItem(item: TopicCardUiModel) {
        binding.title.text = item.title

        binding.subtitle.text = item.subtitle

        if (item.categoryLabel != null) {
            binding.category.text = item.categoryLabel.name
            val bgColor = Color.parseColor( "#${item.categoryLabel.color}")
            binding.categoryColor.setBackgroundColor(bgColor)

            binding.categoryGroup.isVisible = true
        } else {
            binding.categoryGroup.isVisible = false
        }

        imageViews.forEachIndexed { index, imageView ->
            if (index < item.userImageUrlTemplates.size) {
                loadImage(imageView, item.userImageUrlTemplates[index])
                imageView.isVisible = true
            } else {
                imageView.isVisible = false
            }
        }
    }

    private fun loadImage(imageView: ImageView, avatarTemplate: String) {
        if (imageView.measuredWidth == 0) {
            imageView.doOnLayout {
                loadImage(imageView, avatarTemplate, imageView.measuredWidth)
            }
        } else loadImage(imageView, avatarTemplate, imageView.measuredWidth)
    }

    private fun loadImage(imageView: ImageView, avatarTemplate: String, size: Int) {
        picasso.load(avatarTemplate.toUrl(size))
            .transform(CircularTransformation())
            .into(imageView)
    }

    private fun String.toUrl(size: Int): String {
        return "$BASE_URL${replace("{size}", "$size")}"
    }
}