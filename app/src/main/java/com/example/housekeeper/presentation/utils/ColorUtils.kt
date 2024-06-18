package com.example.housekeeper.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.housekeeper.ui.theme.LocalCustomColorsPalette

enum class SpecialColor {
    Success,
    Error,
    ;

    val color: Color
        @Composable
        @ReadOnlyComposable
        get() = when(this) {
            Success -> LocalCustomColorsPalette.current.success
            Error -> LocalCustomColorsPalette.current.error
        }
}