package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface HomeRepository {
    suspend fun getCategories(remoteErrorEmitter: RemoteErrorEmitter): Categories?
}