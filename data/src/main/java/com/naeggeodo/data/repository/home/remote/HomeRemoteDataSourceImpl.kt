package com.naeggeodo.data.repository.home.remote

import com.naeggeodo.data.api.CategoryApi
import com.naeggeodo.data.api.InfoApi
import com.naeggeodo.data.api.SearchChatListByCategoryApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import retrofit2.HttpException
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val categoryApi: CategoryApi,
    private val searchChatListByCategoryApi: SearchChatListByCategoryApi,
    private val infoApi: InfoApi
) : HomeRemoteDataSource, BaseRepository() {
    override suspend fun getCategories(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Categories? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = categoryApi.getCategories()
            if (result.code() != 200) throw HttpException(result)

            result
        }
        return res?.body()
    }

    override suspend fun getChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        category: String?,
        buildingCode: String
    ): ChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = searchChatListByCategoryApi.getChatList(category, buildingCode)
            if (result.code() != 200) throw HttpException(result)

            result
        }
        return res?.body()
    }

    override suspend fun getMyNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyNickName? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = infoApi.getMyNickName(userId)
            if (result.code() != 200) throw HttpException(result)

            result
        }
        return res?.body()
    }
}