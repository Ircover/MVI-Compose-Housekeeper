package com.example.housekeeper.presentation.spend_card

import com.example.housekeeper.presentation.UserMessage

sealed interface SpendCardSideEffect {
    class ShowMessage(val message: UserMessage): SpendCardSideEffect
    class ChangeProductLoadingVisibility(val isVisible: Boolean): SpendCardSideEffect
    object ShowAddProductDialog: SpendCardSideEffect
}