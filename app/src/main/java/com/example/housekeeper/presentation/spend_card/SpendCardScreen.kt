package com.example.housekeeper.presentation.spend_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.housekeeper.R
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.Product
import com.example.housekeeper.presentation.composable.CustomDropdownMenu
import com.example.housekeeper.presentation.composable.PriceTextField
import com.example.housekeeper.presentation.paddingSmall
import com.example.housekeeper.presentation.spacedVerticallyByDefault
import com.example.housekeeper.ui.theme.HousekeeperTheme

@Composable
fun SpendCardScreen(
    modifier: Modifier = Modifier,
    spendCardViewModel: SpendCardViewModel,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        val state = spendCardViewModel.state.collectAsState().value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            RenderSpendCardViewModel(
                state,
                onPriceChanged = { newPrice ->
                    spendCardViewModel.accept(
                        SpendCardUIEvent.PriceChanged(newPrice)
                    )
                },
                onCurrencyChanged = { newCurrency ->
                    spendCardViewModel.accept(
                        SpendCardUIEvent.CurrencyChanged(newCurrency)
                    )
                },
                onProductChanged = { newProduct ->
                    spendCardViewModel.accept(
                        SpendCardUIEvent.ProductChanged(newProduct)
                    )
                }
            )
        }
    }
}

@Composable
private fun RenderSpendCardViewModel(
    state: SpendCardState,
    onPriceChanged: (String) -> Unit,
    onCurrencyChanged: (Currency) -> Unit,
    onProductChanged: (Product) -> Unit,
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
                price = state.price,
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
            )
            Icon(
                modifier = Modifier.paddingSmall(),
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = "add spending",
            )
        }
    }
}

@Preview
@Composable
fun PreviewSpendCardScreen() {
    HousekeeperTheme {
        Surface {
            RenderSpendCardViewModel(
                SpendCardState(
                    price = "100",
                    currency = Currency.Ruble,
                    availableCurrencies = emptyList(),
                    isProductDropdownEnabled = false,
                    product = null,
                    availableProducts = emptyList(),
                ),
                onPriceChanged = { },
                onCurrencyChanged = { },
                onProductChanged = { },
            )
        }
    }
}