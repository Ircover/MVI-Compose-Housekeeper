package com.example.housekeeper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.housekeeper.presentation.spend_card.SpendCardScreen
import com.example.housekeeper.presentation.spend_card.SpendCardViewModel
import com.example.housekeeper.presentation.spending.SpendingScreen
import com.example.housekeeper.presentation.spending.SpendingViewModel
import com.example.housekeeper.ui.theme.HousekeeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HousekeeperTheme {
                val appState = rememberAppState()
                Scaffold(
                    scaffoldState = appState.scaffoldState,
                ) { innerPadding ->
                    NavHost(
                        navController = appState.navController,
                        startDestination = MainDestinations.HOME_ROUTE
                    ) {
                        navigation(
                            route = MainDestinations.HOME_ROUTE,
                            startDestination = MainDestinations.SPENDING,
                        ) {
                            addHomeGraph(
                                modifier = Modifier.padding(innerPadding),
                                navManager = appState.navManager,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController()
) = remember(scaffoldState, navController) {
    AppState(scaffoldState, navController)
}

fun NavGraphBuilder.addHomeGraph(
    modifier: Modifier,
    navManager: NavManager,
) {
    composable(MainDestinations.SPENDING) {
        SpendingScreen(modifier, spendingViewModel = spendingViewModel(navManager))
    }
    composable(MainDestinations.SPEND_CARD) {
        SpendCardScreen(modifier, spendCardViewModel = spendCardViewModel(navManager))
    }
}

@Composable
inline fun <reified T: ViewModel> spendingViewModel(navManager: NavManager) = viewModel<T>(
    factory = viewModelFactory {
        initializer {
            SpendingViewModel(navManager)
        }
    }
)

@Composable
inline fun <reified T: ViewModel> spendCardViewModel(navManager: NavManager) = viewModel<T>(
    factory = viewModelFactory {
        initializer {
            SpendCardViewModel(navManager)
        }
    }
)