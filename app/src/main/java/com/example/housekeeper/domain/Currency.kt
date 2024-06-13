package com.example.housekeeper.domain

sealed class Currency(
    val sign: Char,
) {
    object Ruble : Currency('₽')
    object Dollar : Currency('$')
    object Euro : Currency('€')
}