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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.housekeeper.presentation.ImmutableList
import com.example.housekeeper.presentation.map

data class DropdownMenuItem<T>(
    val value: T,
    val title: String,
)

@Composable
fun <T> T.toDropdownMenuItem(itemFormatter: @Composable (T) -> String) =
    DropdownMenuItem(this, itemFormatter(this))

@Composable
fun <T> CustomDropdownMenu(
    modifier: Modifier,
    selectedItem: T,
    items: ImmutableList<T>,
    itemFormatter: @Composable (T) -> String,
    onItemChanged: (T) -> Unit,
    isEnabled: Boolean = true,
    itemContent: @Composable ((T) -> Unit)? = null,
) {
    CustomDropdownMenu(
        modifier,
        selectedItem.toDropdownMenuItem(itemFormatter),
        items.map { it.toDropdownMenuItem(itemFormatter) },
        onItemChanged,
        isEnabled,
        itemContent
    )
}

@Composable
fun <T> CustomDropdownMenu(
    modifier: Modifier,
    selectedItem: DropdownMenuItem<T>,
    items: ImmutableList<DropdownMenuItem<T>>,
    onItemChanged: (T) -> Unit,
    isEnabled: Boolean = true,
    itemContent: @Composable ((T) -> Unit)? = null,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val currentOnItemChanged by rememberUpdatedState(onItemChanged)
    val currentItemContent by rememberUpdatedState(itemContent)

    val currentIsEnabled by rememberUpdatedState(isEnabled)
    @OptIn(ExperimentalMaterial3Api::class)
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpanded,
        onExpandedChange = { isExpanded = currentIsEnabled && it },
    ) {
        val textModifier = remember {
            Modifier
                .fillMaxWidth()
                .menuAnchor()
        }

        CustomTextField(
            readOnly = true,
            value = TextFieldValue(selectedItem.title),
            onValueChange = { },
            modifier = textModifier,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            },
            enabled = currentIsEnabled,
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            items.items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        currentItemContent?.invoke(item.value) ?: Text(text = item.title)
                    },
                    onClick = {
                        currentOnItemChanged(item.value)
                        isExpanded = false
                    }
                )
            }
        }
    }
}