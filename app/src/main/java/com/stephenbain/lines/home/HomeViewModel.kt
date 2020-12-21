package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.stephenbain.lines.api.LinesApiService
import com.stephenbain.lines.repository.GetLatestApiPagingSource
import com.stephenbain.lines.repository.TopicWithUsers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    api: LinesApiService,
    private val topicToUiModel: TopicToUiModel
) : ViewModel() {

    val data = Pager(config = PagingConfig(pageSize = 30, prefetchDistance = 5)) {
        GetLatestApiPagingSource(api)
    }.flow.map { createUiModel(it) }.cachedIn(viewModelScope)

    private fun createUiModel(pagingData: PagingData<TopicWithUsers>): PagingData<TopicListItemUiModel> {

        fun getSeparator(): TopicListItemUiModel.Separator {
            return TopicListItemUiModel.Separator("title starts with a vowel")
        }

        fun shouldSeparate(item: TopicListItemUiModel.TopicCard?): Boolean {
            val title = item?.title
            return title?.let {
                when {
                    it.startsWith("A") -> true
                    it.startsWith("E") -> true
                    it.startsWith("I") -> true
                    it.startsWith("O") -> true
                    it.startsWith("U") -> true
                    else -> false
                }
            } ?: false
        }


            return pagingData.map { topicToUiModel(it) }.insertSeparators { _, after ->
            if (shouldSeparate(after)) getSeparator()
            else null
        }
    }
}