package com.example.housekeeper.presentation.spend_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.housekeeper.R
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.presentation.composable.AddProductDialog
import com.example.housekeeper.presentation.composable.CustomDropdownMenu
import com.example.housekeeper.presentation.composable.PriceTextField
import com.example.housekeeper.presentation.utils.CustomSnackbarHost
import com.example.housekeeper.presentation.utils.collectState
import com.example.housekeeper.presentation.utils.paddingSmall
import com.example.housekeeper.presentation.utils.show
import com.example.housekeeper.presentation.utils.spacedVerticallyByDefault
import com.example.housekeeper.ui.theme.HousekeeperTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SpendCardScreen(
    modifier: Modifier = Modifier,
    spendCardViewModel: SpendCardViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            CustomSnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        val state = spendCardViewModel.collectState().value
        var isProductLoadingVisible by remember { mutableStateOf(false) }
        var isAddProductDialogVisible by remember { mutableStateOf(false) }
        var isAddProductDialogLoading by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        spendCardViewModel.collectSideEffect {
            when(it) {
                is SpendCardSideEffect.ChangeProductLoadingVisibility ->
                    isProductLoadingVisible = it.isVisible
                is SpendCardSideEffect.ShowMessage -> {
                    snackbarHostState.show(context, coroutineScope, it.message)
                }
                SpendCardSideEffect.ShowAddProductDialog -> isAddProductDialogVisible = true
                SpendCardSideEffect.HideAddProductDialog -> isAddProductDialogVisible = false
                is SpendCardSideEffect.ChangeAddProductDialogLoading ->
                    isAddProductDialogLoading = it.isLoading
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            RenderSpendCardViewModel(
                state,
                isProductLoadingVisible = isProductLoadingVisible,
                onPriceChanged = spendCardViewModel.accept { newPrice ->
                    SpendCardUIEvent.PriceChanged(newPrice)
                },
                onCurrencyChanged = spendCardViewModel.accept { newCurrency ->
                    SpendCardUIEvent.CurrencyChanged(newCurrency)
                },
                onProductChanged = spendCardViewModel.accept { newProduct ->
                    SpendCardUIEvent.ProductChanged(newProduct)
                },
                onProductAddClick = spendCardViewModel.accept {
                    SpendCardUIEvent.AddProductClick
                },
                onProductDeleteClick = spendCardViewModel.accept { product ->
                    SpendCardUIEvent.DeleteProductClick(product)
                },
            )
            if (isAddProductDialogVisible) {
                AddProductDialog(
                    isLoading = isAddProductDialogLoading,
                    onProductAdd = spendCardViewModel.accept { name ->
                        SpendCardUIEvent.AddProduct(name)
                    },
                    onDismissRequest = { isAddProductDialogVisible = false },
                )
            }
        }
    }
}

@Composable
private fun RenderSpendCardViewModel(
    state: SpendCardState,
    isProductLoadingVisible: Boolean,
    onPriceChanged: (TextFieldValue) -> Unit,
    onCurrencyChanged: (Currency) -> Unit,
    onProductChanged: (Product) -> Unit,
    onProductAddClick: () -> Unit,
    onProductDeleteClick: (Product) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedVerticallyByDefault(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.price),
                modifier = Modifier.paddingSmall(),
            )
            PriceTextField(
                price = state.priceFieldValue,
                currency = state.currency,
                onPriceChanged = onPriceChanged,
                modifier = Modifier
                    .paddingSmall()
                    .weight(1f),
            )
            CustomDropdownMenu(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .paddingSmall(),
                selectedItem = state.currency,
                items = state.availableCurrencies,
                itemFormatter = { it.sign.toString() },
                onItemChanged = onCurrencyChanged,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.product),
                modifier = Modifier.paddingSmall(),
            )
            CustomDropdownMenu(
                modifier = Modifier
                    .weight(1f)
                    .paddingSmall(),
                selectedItem = state.product,
                items = state.availableProducts,
                itemFormatter = { it?.name ?: stringResource(R.string.product_empty_placeholder) },
                onItemChanged = { it?.let { notNullProduct -> onProductChanged(notNullProduct) } },
                isEnabled = state.isProductDropdownEnabled,
            ) { product ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .paddingSmall(),
                        text = product?.name ?: stringResource(R.string.product_empty_placeholder),
                    )
                    product?.let {
                        IconButton(onClick = {
                            onProductDeleteClick(product)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                }
            }
            IconButton(
                modifier = Modifier.paddingSmall(),
                onClick = onProductAddClick,
            ) {
                if (isProductLoadingVisible) {
                    CircularProgressIndicator()
                }
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "add spending",
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSpendCardScreen_Default() {
    PreviewSpendCardScreen()
}

@Preview
@Composable
fun PreviewSpendCardScreen_LoadingProduct() {
    PreviewSpendCardScreen(
        isProductLoadingVisible = true,
    )
}

@Composable
private fun PreviewSpendCardScreen(
    isProductLoadingVisible: Boolean = false,
) {
    HousekeeperTheme {
        Surface {
            RenderSpendCardViewModel(
                SpendCardState(
                    priceFieldValue = TextFieldValue("100"),
                    currency = Currency.Ruble,
                    availableCurrencies = emptyList(),
                    isProductDropdownEnabled = false,
                    product = null,
                    availableProducts = emptyList(),
                ),
                isProductLoadingVisible = isProductLoadingVisible,
                onPriceChanged = { },
                onCurrencyChanged = { },
                onProductChanged = { },
                onProductAddClick = { },
                onProductDeleteClick = { },
            )
        }
    }
}