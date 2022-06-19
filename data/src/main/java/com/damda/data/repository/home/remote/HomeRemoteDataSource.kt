package com.damda.data.repository.home.remote

import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface HomeRemoteDataSource {
    suspend fun getCategories(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Categories?
}