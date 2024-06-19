package com.example.housekeeper.presentation.spending

sealed class SpendingUIEvent {
    data object AddSpend : SpendingUIEvent()
}