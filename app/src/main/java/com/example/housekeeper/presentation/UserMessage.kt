package com.example.housekeeper.presentation

import androidx.annotation.StringRes

data class UserMessage(
    @StringRes val textResId: Int,
    val level: UserMessageLevel,
    val duration: UserMessageShowDuration,
)

enum class UserMessageLevel {
    Success,
    Info,
    Error,
}

sealed interface UserMessageShowDuration {
    data object Short: UserMessageShowDuration
    data object Long: UserMessageShowDuration
    data object Indefinite: UserMessageShowDuration
}