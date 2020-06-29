package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.stephenbain.lines.api.LinesApiService
import com.stephenbain.lines.api.Topic
import com.stephenbain.lines.repository.getlatest.GetLatestApiPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(api: LinesApiService) : ViewModel() {

    private val pager = Pager(config = PagingConfig(pageSize = 30, prefetchDistance = 15)) {
        GetLatestApiPagingSource(api)
    }

    val data = pager.flow.map { createUiModel(it) }.cachedIn(viewModelScope)

    private fun createUiModel(pagingData: PagingData<Topic>): PagingData<HomeItemUiModel> {
        return pagingData.map { HomeItemUiModel.TopicItem(it) }.insertSeparators { _, _ ->
            HomeItemUiModel.Separator
        }
    }

}

// may add more customized separator later
sealed class HomeItemUiModel {
    data class TopicItem(val topic: Topic) : HomeItemUiModel()
    object Separator : HomeItemUiModel()
}