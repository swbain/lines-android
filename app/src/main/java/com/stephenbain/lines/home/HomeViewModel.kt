package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.stephenbain.lines.repository.CategoryItem
import com.stephenbain.lines.repository.GetLatestTopicsRepository
import com.stephenbain.lines.repository.TopicWithUsers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(topicsRepository: GetLatestTopicsRepository) : ViewModel() {

    val data = topicsRepository.getTopics(CategoryItem.AllCategories)
        .map { createUiModel(it) }
        .asLiveData(viewModelScope.coroutineContext)

    private fun createUiModel(pagingData: PagingData<TopicWithUsers>): PagingData<HomeItemUiModel> {
        return pagingData.map { HomeItemUiModel.TopicItem(it) }
    }

}

// may add more customized separator later
sealed class HomeItemUiModel {
    data class TopicItem(val topic: TopicWithUsers) : HomeItemUiModel()
}