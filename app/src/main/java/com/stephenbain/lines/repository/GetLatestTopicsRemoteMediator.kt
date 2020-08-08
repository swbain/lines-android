package com.stephenbain.lines.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator

@ExperimentalPagingApi
class GetLatestTopicsRemoteMediator : RemoteMediator<Int, TopicWithUsersAndCategory>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TopicWithUsersAndCategory>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
}