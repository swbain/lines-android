package com.stephenbain.lines.home

import android.content.Context
import com.stephenbain.lines.R
import com.stephenbain.lines.repository.TopicWithUsersAndCategory
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

data class TopicCardUiModel(
    val id: Long,
    val title: String,
    val userImageUrlTemplates: List<String>,
    val subtitle: String,
    val categoryLabel: CategoryLabel?
)

data class CategoryLabel(val name: String, val color: String)

class TopicToUiModel @Inject constructor(context: Context) : (TopicWithUsersAndCategory) -> TopicCardUiModel {

    private val resources = context.resources

    override fun invoke(topic: TopicWithUsersAndCategory): TopicCardUiModel = TopicCardUiModel(
        id = topic.id,
        title = topic.title,
        userImageUrlTemplates = topic.users.map { it.avatarTemplate },
        subtitle = getSubtitle(topic),
        categoryLabel = topic.category?.let { CategoryLabel(it.name, it.color) }
    )

    private fun getSubtitle(topic: TopicWithUsersAndCategory): String {
        val replies = resources.getQuantityString(
            R.plurals.replies_label,
            topic.postCount - 1,
            (topic.postCount - 1).toFormattedCount()
        )
        val views = resources.getQuantityString(
            R.plurals.views_label,
            topic.views,
            topic.views.toFormattedCount()
        )

        return resources.getString(
            R.string.topic_card_subtitle,
            replies,
            views,
            topic.lastPostedAt.toFormattedString()
        )
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

    private fun Date.toFormattedString(): String {
        val diffMillis = System.currentTimeMillis() - time
        val diffSeconds = diffMillis / 1000F
        val diffMinutes = diffSeconds / 60F
        val diffHours = diffMinutes / 60F
        val diffDays = diffHours / 24F

        return if (diffDays >= 31) "last activity ${toMonthString()}"
        else {
            val stuff = when {
                diffDays >= 1 -> "${diffDays.roundToInt()}d"
                diffHours >= 1 -> "${diffHours.roundToInt()}h"
                else -> "${diffMinutes.roundToInt()}m"
            }

            "last activity $stuff ago"
        }
    }

    private fun Date.toMonthString(): String {
        val localDate = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val month = localDate.monthValue
        val day = localDate.dayOfMonth
        val year = localDate.year
        val thisYear = LocalDate.now().year

        return if (year == thisYear) "${getMonthString(month)} $day"
        else "${getMonthString(month)} '$year"
    }

    private fun getMonthString(month: Int): String {
        return when (month) {
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "Aug"
            8 -> "Sept"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> throw IllegalStateException("unsupported month value")
        }
    }
}