package com.example.housekeeper.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableList<out T>(val items: List<T>)

inline fun <T, R> ImmutableList<T>.map(transform: (T) -> R) = items.map(transform).toImmutable()

fun <T> List<T>.toImmutable() = ImmutableList(this)