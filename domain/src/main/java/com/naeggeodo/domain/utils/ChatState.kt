package com.naeggeodo.domain.utils

enum class ChatState {
    CREATE,         // 생성됨
    PROGRESS,       // 진행중(미사용)
    END,            // 종료됨
    FULL,           // 인원수 꽉 참
    INCOMPLETE      // 방 터짐(방이 만들어진 상태에서 전부 나간 경우)
}