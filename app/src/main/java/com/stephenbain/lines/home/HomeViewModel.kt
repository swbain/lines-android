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
import kotlin.random.Random

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

        fun shouldSeparate(): Boolean {
            return Random.nextInt(4) == 1
        }

        fun getSeparator(): TopicListItemUiModel.Separator {
            return TopicListItemUiModel.Separator("fake separator!!")
        }

        return pagingData.map { topicToUiModel(it) }.insertSeparators { before, after ->
            if (shouldSeparate()) getSeparator()
            else null
        }


    }
}