package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.stephenbain.lines.repository.GetLatestTopicsRepository
import com.stephenbain.lines.repository.TopicWithUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import kotlin.random.Random

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    topicsRepository: GetLatestTopicsRepository,
    private val topicToUiModel: TopicToUiModel
) : ViewModel() {

    val data = topicsRepository.getTopics()
        .map { createUiModel(it) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

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