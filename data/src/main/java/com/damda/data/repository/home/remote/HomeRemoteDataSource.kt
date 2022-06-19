package com.damda.data.repository.home.remote

import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface HomeRemoteDataSource {
    suspend fun getCategories(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Category?
}