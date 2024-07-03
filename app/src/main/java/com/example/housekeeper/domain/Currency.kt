package com.example.housekeeper.domain

import androidx.compose.runtime.Stable

@Stable
sealed class Currency(
    val sign: Char,
) {
    data object Ruble : Currency('₽')
    data object Dollar : Currency('$')
    data object Euro : Currency('€')
}