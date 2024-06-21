package com.example.housekeeper.presentation.spend_card

import com.example.housekeeper.presentation.UserMessage

sealed interface SpendCardSideEffect {
    class ShowMessage(val message: UserMessage): SpendCardSideEffect
    class ChangeProductLoadingVisibility(val isVisible: Boolean): SpendCardSideEffect
    class ChangeAddProductDialogLoading(val isLoading: Boolean): SpendCardSideEffect
    data object ShowAddProductDialog: SpendCardSideEffect
    data object HideAddProductDialog: SpendCardSideEffect
}