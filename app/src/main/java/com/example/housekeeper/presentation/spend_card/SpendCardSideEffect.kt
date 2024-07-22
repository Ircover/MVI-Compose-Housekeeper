package com.example.housekeeper.presentation.spend_card

import com.example.housekeeper.presentation.UserMessage

sealed interface SpendCardSideEffect {
    class ShowMessage(val message: UserMessage): SpendCardSideEffect
    class ShowProductDialogMessage(val message: UserMessage): SpendCardSideEffect
    class ShowShopDialogMessage(val message: UserMessage): SpendCardSideEffect
    data object ShowAddProductDialog: SpendCardSideEffect
    data object HideAddProductDialog: SpendCardSideEffect
    data object ShowAddShopDialog: SpendCardSideEffect
    data object HideAddShopDialog: SpendCardSideEffect
    sealed class ChangeLoadingVisibility(val isLoading: Boolean) : SpendCardSideEffect {
        class ChangeAddProductDialogLoading(isLoading: Boolean): ChangeLoadingVisibility(isLoading)
        class ChangeAddShopDialogLoading(isLoading: Boolean): ChangeLoadingVisibility(isLoading)
        class ChangeGlobalLoading(isLoading: Boolean): ChangeLoadingVisibility(isLoading)
    }
}