package com.stephenbain.lines.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Success<T>(val value: T): Resource<T>()
    data class Error<T>(val t: Throwable): Resource<T>()
}

@ExperimentalCoroutinesApi
fun <T> Flow<T>.toResource(): Flow<Resource<T>> {
    return flow {
        emit(Resource.Loading())
        try {
            emitAll(map { Resource.Success(it) })
        } catch (t: Throwable) {
            emit(Resource.Error<T>(t))
        }

    }
}