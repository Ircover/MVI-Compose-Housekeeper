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
import com.example.housekeeper.domain.product.usecase.AddProductUsecase
import com.example.housekeeper.domain.product.usecase.GetProductsUsecase

@Stable
class AppState(
    val applicationContext: Context,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    val navManager = NavManagerImpl(navController)
    val database: AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "app-database",
    ).build()
    val productRepository = ProductRepositoryImpl(database.productDao())
    val viewModelsProvider = ViewModelsProvider(
        navManager,
        AddProductUsecase(productRepository),
        GetProductsUsecase(productRepository),
    )
    val currentRoute: String?
        get() = navController.currentDestination?.route
}

fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED