package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenbain.lines.api.TopicJson
import com.stephenbain.lines.common.Resource
import com.stephenbain.lines.common.toResource
import com.stephenbain.lines.repository.GetLatestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(getLatestRepo: GetLatestRepository) :
    ViewModel() {

    val topics = getLatestRepo.getLatestTopics()
        .toResource()
        .flowOn(Dispatchers.IO)
        .asLiveData(viewModelScope.coroutineContext)

}