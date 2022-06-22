package com.damda.data.repository.login

import com.damda.data.repository.login.remote.LoginRemoteDataSource
import com.naeggeodo.data.repository.search.remote.SearchRemoteDataSource
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.model.Tags
import com.naeggeodo.domain.repository.LoginRepository
import com.naeggeodo.domain.repository.SearchRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchRepository {
    override suspend fun getTags(remoteErrorEmitter: RemoteErrorEmitter): Tags? {
        return searchRemoteDataSource.getTags(remoteErrorEmitter)
    }

    override suspend fun getChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        searchType: String,
        keyWord: String
    ): ChatList? {
        return searchRemoteDataSource.getChatList(remoteErrorEmitter, searchType, keyWord)
    }
}