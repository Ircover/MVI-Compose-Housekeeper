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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.housekeeper.presentation.spend_card.SpendCardScreen
import com.example.housekeeper.presentation.spending.SpendingScreen
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
                                viewModelsProvider = appState.viewModelsProvider,
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
): AppState {
    val context = LocalContext.current
    return remember(scaffoldState, navController) {
        AppState(context, scaffoldState, navController)
    }
}

fun NavGraphBuilder.addHomeGraph(
    modifier: Modifier,
    viewModelsProvider: ViewModelsProvider,
) {
    composable(MainDestinations.SPENDING) {
        SpendingScreen(modifier, spendingViewModel = viewModelsProvider.get())
    }
    composable(MainDestinations.SPEND_CARD) {
        SpendCardScreen(modifier, spendCardViewModel = viewModelsProvider.get())
    }
}