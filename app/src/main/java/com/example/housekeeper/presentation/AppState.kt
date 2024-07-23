package com.example.housekeeper.presentation

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Stable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.housekeeper.data.AppDatabase
import com.example.housekeeper.data.product.ProductRepositoryImpl
import com.example.housekeeper.data.shop.ShopRepositoryImpl
import com.example.housekeeper.data.spend.SpendRepositoryImpl
import com.example.housekeeper.domain.product.usecase.AddProductUsecase
import com.example.housekeeper.domain.product.usecase.GetProductsUsecase
import com.example.housekeeper.domain.shop.usecase.AddShopUsecase
import com.example.housekeeper.domain.shop.usecase.GetShopsUsecase
import com.example.housekeeper.domain.spend.usecase.SaveSpendUsecase

@Stable
class AppState(
    val applicationContext: Context,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    val navManager = NavManagerImpl(navController)
    val dateManager = DateManager()
    val database: AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "app-database",
    ).build()
    val productRepository = ProductRepositoryImpl(database.productDao())
    val shopRepository = ShopRepositoryImpl(database.shopDao())
    val spendRepository = SpendRepositoryImpl(database.spendDao())
    val viewModelsProvider = ViewModelsProvider(
        navManager,
        AddProductUsecase(productRepository),
        GetProductsUsecase(productRepository),
        AddShopUsecase(shopRepository),
        GetShopsUsecase(shopRepository),
        SaveSpendUsecase(spendRepository),
        dateManager,
    )
    val currentRoute: String?
        get() = navController.currentDestination?.route
}

fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED