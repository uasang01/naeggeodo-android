package com.naeggeodo.data.repository.search.remote

import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.data.api.GetTagsApi
import com.naeggeodo.data.api.SearchChatListByKeyWordApi
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.Tags
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import timber.log.Timber
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(
    private val getTagsApi: GetTagsApi,
    private val searchChatListByKeyWordApi: SearchChatListByKeyWordApi
) : SearchRemoteDataSource, BaseRepository() {
    override suspend fun getTags(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Tags? {
        val res = safeApiCall(remoteErrorEmitter) {
            getTagsApi.getTags()
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }

    override suspend fun getChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        searchType: String,
        keyWord: String
    ): ChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            searchChatListByKeyWordApi.getChatList(searchType, keyWord)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }
}