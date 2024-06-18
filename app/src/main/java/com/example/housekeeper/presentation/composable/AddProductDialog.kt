package com.example.housekeeper.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.CustomTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.housekeeper.R
import com.example.housekeeper.presentation.utils.MediumRoundedCornerShape
import com.example.housekeeper.presentation.utils.paddingMedium
import com.example.housekeeper.presentation.utils.paddingSmall
import com.example.housekeeper.ui.theme.HousekeeperTheme
import com.example.housekeeper.ui.theme.Typography

@Composable
fun AddProductDialog(
    isLoading: Boolean,
    onProductAdd: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        AddProductDialogContent(isLoading, onProductAdd, onDismissRequest)
    }
}

@Composable
fun AddProductDialogContent(
    isLoading: Boolean,
    onProductAdd: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var result by remember { mutableStateOf(TextFieldValue()) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MediumRoundedCornerShape(),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd,
        ) {
            IconButton(
                onClick = { onDismissRequest() },
            ) {
                Icon(Icons.Filled.Close, contentDescription = "close")
            }
            Column(
                modifier = Modifier.paddingMedium(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.add_product_title),
                    style = Typography.headlineSmall,
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                CustomTextField(
                    value = result,
                    onValueChange = { newValue -> result = newValue },
                    label = {
                        Text(
                            text = stringResource(R.string.add_product_name_label),
                        )
                    },
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(),
                )
                Button(
                    onClick = { onProductAdd(result.text) },
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(),
                ) {
                    Box(
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = stringResource(R.string.add),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.alpha(if (isLoading) 1f else 0f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddProductDialog_Default() {
    PreviewAddProductDialog(isLoading = false)
}

@Preview
@Composable
fun PreviewAddProductDialog_Loading() {
    PreviewAddProductDialog(isLoading = true)
}

@Composable
private fun PreviewAddProductDialog(isLoading: Boolean) {
    HousekeeperTheme {
        Surface {
            AddProductDialogContent(
                isLoading = isLoading,
                onProductAdd = { },
                onDismissRequest = { },
            )
        }
    }
}