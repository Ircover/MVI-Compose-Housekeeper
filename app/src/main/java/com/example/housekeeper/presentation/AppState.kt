package com.example.housekeeper.presentation

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Stable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Stable
class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    val navManager = NavManagerImpl(navController)
    val currentRoute: String?
        get() = navController.currentDestination?.route
}

fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED