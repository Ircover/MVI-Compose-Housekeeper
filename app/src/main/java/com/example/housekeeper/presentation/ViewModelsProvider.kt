package com.example.housekeeper.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.housekeeper.domain.product.usecase.AddProductUsecase
import com.example.housekeeper.domain.product.usecase.GetProductsUsecase
import com.example.housekeeper.domain.shop.usecase.AddShopUsecase
import com.example.housekeeper.domain.shop.usecase.GetShopsUsecase
import com.example.housekeeper.domain.spend.usecase.SaveSpendUsecase
import com.example.housekeeper.presentation.spend_card.SpendCardViewModel
import com.example.housekeeper.presentation.spending.SpendingViewModel

class ViewModelsProvider(
    private val navManager: NavManager,
    private val addProductUsecase: AddProductUsecase,
    private val getProductsUsecase: GetProductsUsecase,
    private val addShopUsecase: AddShopUsecase,
    private val getShopsUsecase: GetShopsUsecase,
    private val saveSpendUsecase: SaveSpendUsecase,
    private val dateManager: DateManager,
) {
    val factory = viewModelFactory {
        initializer { SpendingViewModel(navManager) }
        initializer {
            SpendCardViewModel(
                addProductUsecase,
                getProductsUsecase,
                addShopUsecase,
                getShopsUsecase,
                saveSpendUsecase,
                navManager,
                dateManager,
            )
        }
    }
    @Composable
    inline fun <reified T: ViewModel> get(): T {
        return viewModel<T>(factory = factory)
    }
}