package com.example.housekeeper.presentation.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun DateTextField(
    modifier: Modifier = Modifier,
    date: String,
    onClick: () -> Unit,
) {
    val source = remember {
        MutableInteractionSource()
    }
    CustomTextField(
        value = TextFieldValue(date),
        onValueChange = { },
        readOnly = true,
        modifier = modifier,
        interactionSource = source,
        leadingIcon = {
            Icon(Icons.Filled.DateRange, contentDescription = null)
        }
    )
    if (source.collectIsPressedAsState().value) {
        onClick()
    }
}