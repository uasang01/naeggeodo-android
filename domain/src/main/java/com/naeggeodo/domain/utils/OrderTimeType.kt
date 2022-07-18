package com.naeggeodo.domain.utils

enum class OrderTimeType(
    val korean: String
) {
    ONE_HOUR("1시간 이내"),       // 한 시간 이내
    QUICK("최대한 빨리"),          // 가능 한 빨리
    FREEDOM("모집되는대로")         // 상관 없음
}