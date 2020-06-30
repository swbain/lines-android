package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import com.stephenbain.lines.api.Topic
import com.stephenbain.lines.repository.CategoriesRepository
import com.stephenbain.lines.repository.CategoryItem
import com.stephenbain.lines.repository.GetLatestTopicsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    categoriesRepository: CategoriesRepository,
    topicsRepository: GetLatestTopicsRepository
) : ViewModel() {

    private val selectedCategory = ConflatedBroadcastChannel<CategoryItem>(CategoryItem.AllCategories)

    val data = selectedCategory.asFlow().distinctUntilChanged().flatMapLatest { category ->
        topicsRepository.getTopics(category).map { createUiModel(it) }
    }.asLiveData(viewModelScope.coroutineContext)

    val categories = categoriesRepository.getCategories().asLiveData(viewModelScope.coroutineContext)

    fun setSelectedCategory(category: CategoryItem) = viewModelScope.launch {
        selectedCategory.send(category)
    }

    private fun createUiModel(pagingData: PagingData<Topic>): PagingData<HomeItemUiModel> {
        return pagingData.map { HomeItemUiModel.TopicItem(it) }
            .insertSeparators { _, _ -> HomeItemUiModel.Separator }
    }

}

// may add more customized separator later
sealed class HomeItemUiModel {
    data class TopicItem(val topic: Topic) : HomeItemUiModel()
    object Separator : HomeItemUiModel()
}