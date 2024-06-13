package com.example.housekeeper.presentation

import androidx.navigation.NavHostController

interface NavManager {
    fun openSpendCard()
}

class NavManagerImpl(private val navController: NavHostController): NavManager {
    override fun openSpendCard() {
        navController.navigate(MainDestinations.SPEND_CARD)
    }
}