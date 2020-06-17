package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenbain.lines.common.api.TopicJson
import com.stephenbain.lines.common.repository.GetLatestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(getLatestRepo: GetLatestRepository) :
    ViewModel() {

    private val topics = getLatestRepo.getLatestTopics()
        .flowOn(Dispatchers.IO)
        .asLiveData(viewModelScope.coroutineContext)

    private val loading = MediatorLiveData<Boolean>()

    init {
        loading.value = true
        loading.addSource(topics) {
            loading.value = false
            loading.removeSource(topics)
        }
    }

    fun topics(): LiveData<List<TopicJson>> = topics

    fun loading(): LiveData<Boolean> = loading

}