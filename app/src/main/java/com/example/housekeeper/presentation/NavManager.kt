package com.example.housekeeper.presentation

import androidx.annotation.MainThread
import androidx.navigation.NavHostController

interface NavManager {
    fun openSpendCard()
    @MainThread
    fun goBack()
}

class NavManagerImpl(private val navController: NavHostController): NavManager {
    override fun openSpendCard() {
        navController.navigate(MainDestinations.SPEND_CARD)
    }

    override fun goBack() {
        navController.popBackStack()
    }
}