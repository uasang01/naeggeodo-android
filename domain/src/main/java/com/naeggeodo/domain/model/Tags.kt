package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

class Tags(
    @SerializedName("tags")
    val tags: List<Tag>,
)