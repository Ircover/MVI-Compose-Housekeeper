package com.example.housekeeper.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CustomTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun <T> CustomDropdownMenu(
    modifier: Modifier,
    selectedItem: T,
    items: List<T>,
    itemFormatter: @Composable (T) -> String,
    onItemChanged: (T) -> Unit,
    isEnabled: Boolean = true,
) {
    var isExpandedState by remember { mutableStateOf(false) }
    @OptIn(ExperimentalMaterial3Api::class)
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpandedState,
        onExpandedChange = { isExpandedState = isEnabled && it },
    ) {
        CustomTextField(
            readOnly = true,
            value = TextFieldValue( itemFormatter(selectedItem)),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpandedState
                )
            },
            enabled = isEnabled,
        )
        ExposedDropdownMenu(
            expanded = isExpandedState,
            onDismissRequest = { isExpandedState = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = itemFormatter(item))
                    },
                    onClick = {
                        onItemChanged(item)
                        isExpandedState = false
                    }
                )
            }
        }
    }
}