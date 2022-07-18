package com.naeggeodo.data.repository.search.remote

import com.naeggeodo.data.api.GetTagsApi
import com.naeggeodo.data.api.SearchChatListByKeyWordApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.Tags
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import retrofit2.HttpException
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(
    private val getTagsApi: GetTagsApi,
    private val searchChatListByKeyWordApi: SearchChatListByKeyWordApi
) : SearchRemoteDataSource, BaseRepository() {
    override suspend fun getTags(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Tags? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = getTagsApi.getTags()
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun getChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        searchType: String,
        keyWord: String
    ): ChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = searchChatListByKeyWordApi.getChatList(searchType, keyWord)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }
}