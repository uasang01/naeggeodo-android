package com.naeggeodo.domain.model


import com.google.gson.annotations.SerializedName

data class Chat(
    val address: String,
    val bookmarks: Any,
    val bookmarksDate: Any,
    val buildingCode: String,
    val category: String,
    val createDate: String,
    val currentCount: Int,
    val endDate: Any,
    @SerializedName("id")
    val chatId: Int,
    val idx: Int,
    val imgPath: String,
    val link: String,
    val maxCount: Int,
    val orderTimeType: String,
    val place: Any,
    val state: String,
    val tags: List<String>,
    val title: String,
    @SerializedName("user_id")
    val userId: String
)