package com.example.housekeeper.presentation.spend_card

import com.example.housekeeper.presentation.UserMessage

sealed interface SpendCardSideEffect {
    class ShowMessage(val message: UserMessage): SpendCardSideEffect
    class ShowProductDialogMessage(val message: UserMessage): SpendCardSideEffect
    class ShowShopDialogMessage(val message: UserMessage): SpendCardSideEffect
    class ChangeAddProductDialogLoading(val isLoading: Boolean): SpendCardSideEffect
    data object ShowAddProductDialog: SpendCardSideEffect
    data object HideAddProductDialog: SpendCardSideEffect
    class ChangeAddShopDialogLoading(val isLoading: Boolean): SpendCardSideEffect
    data object ShowAddShopDialog: SpendCardSideEffect
    data object HideAddShopDialog: SpendCardSideEffect
}