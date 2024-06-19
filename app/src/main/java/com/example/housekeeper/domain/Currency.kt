package com.example.housekeeper.domain

sealed class Currency(
    val sign: Char,
) {
    data object Ruble : Currency('₽')
    data object Dollar : Currency('$')
    data object Euro : Currency('€')
}