package com.example.housekeeper.presentation

import androidx.annotation.StringRes

data class UserMessage(
    @StringRes val textResId: Int,
    val level: UserMessageLevel,
    val duration: UserMessageShowDuration,
)

enum class UserMessageLevel {
    Success,
    Error,
}

sealed interface UserMessageShowDuration {
    object Short: UserMessageShowDuration
    object Long: UserMessageShowDuration
    object Indefinite: UserMessageShowDuration
}