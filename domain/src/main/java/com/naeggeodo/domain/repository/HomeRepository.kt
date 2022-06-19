package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface HomeRepository {
    suspend fun getCategories(remoteErrorEmitter: RemoteErrorEmitter): Category?
}