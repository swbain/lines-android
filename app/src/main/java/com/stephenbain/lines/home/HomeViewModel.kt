package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.stephenbain.lines.repository.CategoryItem
import com.stephenbain.lines.repository.GetLatestTopicsRepository
import com.stephenbain.lines.repository.TopicWithUsersAndCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.observeOn

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    topicsRepository: GetLatestTopicsRepository,
    private val topicToUiModel: TopicToUiModel
) : ViewModel() {

    val data = topicsRepository.getTopics(CategoryItem.AllCategories)
        .map { createUiModel(it) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private fun createUiModel(pagingData: PagingData<TopicWithUsersAndCategory>): PagingData<TopicCardUiModel> {
        return pagingData.map { topicToUiModel(it) }
    }
}