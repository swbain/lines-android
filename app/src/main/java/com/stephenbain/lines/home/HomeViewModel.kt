package com.stephenbain.lines.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenbain.lines.common.api.Topic
import com.stephenbain.lines.common.repository.GetLatestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(getLatestRepo: GetLatestRepository) :
    ViewModel() {

    val state: LiveData<HomeState> = getLatestRepo.getLatestTopics()
        .map { HomeState(loading = false, topics = it) }
        .onStart { emit(HomeState(loading = true, topics = emptyList())) }
        .flowOn(Dispatchers.IO)
        .asLiveData(viewModelScope.coroutineContext)

}

data class HomeState(val loading: Boolean, val topics: List<Topic>)