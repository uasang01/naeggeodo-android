package com.damda.data.repository.home.remote

import com.damda.data.api.CategoryApi
import com.damda.data.base.BaseRepository
import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import timber.log.Timber
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val categoryApi: CategoryApi
) : HomeRemoteDataSource, BaseRepository() {
    override suspend fun getCategories(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Categories? {
        val res = safeApiCall(remoteErrorEmitter) {
            categoryApi.getCategories()
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }
}