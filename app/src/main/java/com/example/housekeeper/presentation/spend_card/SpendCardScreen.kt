package com.example.housekeeper.presentation.spend_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CustomTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.AddCircle
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.housekeeper.R
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.presentation.composable.AddProductDialog
import com.example.housekeeper.presentation.composable.AddShopDialog
import com.example.housekeeper.presentation.composable.CustomDropdownMenu
import com.example.housekeeper.presentation.composable.PriceTextField
import com.example.housekeeper.presentation.composable.RadioTextButton
import com.example.housekeeper.presentation.emptyImmutableList
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
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            CustomSnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        val onPriceChanged = remember {
            spendCardViewModel.accept { newPrice: TextFieldValue ->
                SpendCardUIEvent.PriceChanged(newPrice)
            }
        }
        val onCurrencyChanged = remember {
            spendCardViewModel.accept { newCurrency: Currency ->
                SpendCardUIEvent.CurrencyChanged(newCurrency)
            }
        }
        val onAmountTextChanged = remember {
            spendCardViewModel.accept { newAmount: TextFieldValue ->
                SpendCardUIEvent.AmountChanged(newAmount)
            }
        }
        val onAmountTypeChanged = remember {
            spendCardViewModel.accept { newType: AmountType ->
                SpendCardUIEvent.AmountTypeChanged(newType)
            }
        }
        val onProductChanged = remember {
            spendCardViewModel.accept { newProduct: Product? ->
                SpendCardUIEvent.ProductChanged(newProduct)
            }
        }
        val onProductAddClick = remember {
            spendCardViewModel.accept {
                SpendCardUIEvent.AddProductClick
            }
        }
        val onProductDeleteClick = remember {
            spendCardViewModel.accept { product: Product ->
                SpendCardUIEvent.DeleteProductClick(product)
            }
        }
        val onShopChanged = remember {
            spendCardViewModel.accept { newShop: Shop? ->
                SpendCardUIEvent.ShopChanged(newShop)
            }
        }
        val onShopAddClick = remember {
            spendCardViewModel.accept {
                SpendCardUIEvent.AddShopClick
            }
        }
        val onShopDeleteClick = remember {
            spendCardViewModel.accept { shop: Shop ->
                SpendCardUIEvent.DeleteShopClick(shop)
            }
        }
        SpendCardScreen(
            innerPadding,
            snackbarHostState,
            spendCardViewModel,
            onPriceChanged,
            onCurrencyChanged,
            onAmountTextChanged,
            onAmountTypeChanged,
            onProductChanged,
            onProductAddClick,
            onProductDeleteClick,
            onShopChanged,
            onShopAddClick,
            onShopDeleteClick,
        )
    }
}
@Composable
private fun SpendCardScreen(
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    spendCardViewModel: SpendCardViewModel,
    onPriceChanged: (TextFieldValue) -> Unit,
    onCurrencyChanged: (Currency) -> Unit,
    onAmountTextChanged: (TextFieldValue) -> Unit,
    onAmountTypeChanged: (AmountType) -> Unit,
    onProductChanged: (Product?) -> Unit,
    onProductAddClick: () -> Unit,
    onProductDeleteClick: (Product) -> Unit,
    onShopChanged: (Shop?) -> Unit,
    onShopAddClick: () -> Unit,
    onShopDeleteClick: (Shop) -> Unit,
) {
    val context = LocalContext.current
    val state = spendCardViewModel.collectState().value
    var isAddProductDialogVisible by remember { mutableStateOf(false) }
    var isAddProductDialogLoading by remember { mutableStateOf(false) }
    var isAddShopDialogVisible by remember { mutableStateOf(false) }
    var isAddShopDialogLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    spendCardViewModel.collectSideEffect {
        when (it) {
            is SpendCardSideEffect.ShowMessage -> {
                snackbarHostState.show(context, coroutineScope, it.message)
            }
            SpendCardSideEffect.ShowAddProductDialog -> isAddProductDialogVisible = true
            SpendCardSideEffect.HideAddProductDialog -> isAddProductDialogVisible = false
            is SpendCardSideEffect.ChangeAddProductDialogLoading -> {
                isAddProductDialogLoading = it.isLoading
            }
            is SpendCardSideEffect.ChangeAddShopDialogLoading -> {
                isAddShopDialogLoading = it.isLoading
            }
            SpendCardSideEffect.HideAddShopDialog -> isAddShopDialogVisible = false
            SpendCardSideEffect.ShowAddShopDialog -> isAddShopDialogVisible = true
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
            onPriceChanged = onPriceChanged,
            onCurrencyChanged = onCurrencyChanged,
            onAmountTextChanged = onAmountTextChanged,
            onAmountTypeChanged = onAmountTypeChanged,
            onProductChanged = onProductChanged,
            onProductAddClick = onProductAddClick,
            onProductDeleteClick = onProductDeleteClick,
            onShopChanged = onShopChanged,
            onShopAddClick = onShopAddClick,
            onShopDeleteClick = onShopDeleteClick,
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
        if (isAddShopDialogVisible) {
            AddShopDialog(
                isLoading = isAddShopDialogLoading,
                onShopAdd = spendCardViewModel.accept { name ->
                    SpendCardUIEvent.AddShop(name)
                },
                onDismissRequest = { isAddShopDialogVisible = false },
            )
        }
    }
}

@Composable
private fun RenderSpendCardViewModel(
    state: SpendCardState,
    onPriceChanged: (TextFieldValue) -> Unit = { },
    onCurrencyChanged: (Currency) -> Unit = { },
    onAmountTextChanged: (TextFieldValue) -> Unit = { },
    onAmountTypeChanged: (AmountType) -> Unit = { },
    onProductChanged: (Product?) -> Unit = { },
    onProductAddClick: () -> Unit = { },
    onProductDeleteClick: (Product) -> Unit = { },
    onShopChanged: (Shop?) -> Unit = { },
    onShopAddClick: () -> Unit = { },
    onShopDeleteClick: (Shop) -> Unit = { },
) {
    val currentOnPriceChanged by rememberUpdatedState(onPriceChanged)
    val currentOnCurrencyChanged by rememberUpdatedState(onCurrencyChanged)
    val currentOnAmountTextChanged by rememberUpdatedState(onAmountTextChanged)
    val currentOnAmountTypeChanged by rememberUpdatedState(onAmountTypeChanged)
    val currentOnProductChanged by rememberUpdatedState(onProductChanged)
    val currentOnProductAddClick by rememberUpdatedState(onProductAddClick)
    val currentOnProductDeleteClick by rememberUpdatedState(onProductDeleteClick)
    val currentOnShopChanged by rememberUpdatedState(onShopChanged)
    val currentOnShopAddClick by rememberUpdatedState(onShopAddClick)
    val currentOnShopDeleteClick by rememberUpdatedState(onShopDeleteClick)

    Column(
        verticalArrangement = Arrangement.spacedVerticallyByDefault(),
    ) {
        //Цена
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
                onPriceChanged = currentOnPriceChanged,
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
                onItemChanged = currentOnCurrencyChanged,
            )
        }
        //Количество
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.amount),
                modifier = Modifier.paddingSmall(),
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomTextField(
                    value = state.amountFieldValue,
                    onValueChange = currentOnAmountTextChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingSmall(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row {
                    RadioTextButton(
                        isSelected = state.amountType == AmountType.Count,
                        title = stringResource(R.string.radio_count),
                        modifier = Modifier.weight(1f),
                        onClick = { currentOnAmountTypeChanged(AmountType.Count) },
                    )
                    RadioTextButton(
                        isSelected = state.amountType == AmountType.Weight,
                        title = stringResource(R.string.radio_weigth),
                        modifier = Modifier.weight(1f),
                        onClick = { currentOnAmountTypeChanged(AmountType.Weight) },
                    )
                }
            }
        }
        //Товар
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
                onItemChanged = currentOnProductChanged,
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
                            currentOnProductDeleteClick(product)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                }
            }
            IconButton(
                modifier = Modifier.paddingSmall(),
                onClick = currentOnProductAddClick,
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "add spending product",
                )
            }
        }
        //Магазин
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.shop),
                modifier = Modifier.paddingSmall(),
            )
            CustomDropdownMenu(
                modifier = Modifier
                    .weight(1f)
                    .paddingSmall(),
                selectedItem = state.shop,
                items = state.availableShops,
                itemFormatter = { it?.name ?: stringResource(R.string.shop_empty_placeholder) },
                onItemChanged = currentOnShopChanged,
                isEnabled = state.isShopDropdownEnabled,
            ) { shop ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .paddingSmall(),
                        text = shop?.name ?: stringResource(R.string.shop_empty_placeholder),
                    )
                    shop?.let {
                        IconButton(onClick = {
                            currentOnShopDeleteClick(shop)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                }
            }
            IconButton(
                modifier = Modifier.paddingSmall(),
                onClick = currentOnShopAddClick,
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "add spending shop",
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSpendCardScreen_Default() {
    HousekeeperTheme {
        Surface {
            RenderSpendCardViewModel(
                SpendCardState(
                    priceFieldValue = TextFieldValue("100"),
                    currency = Currency.Ruble,
                    availableCurrencies = emptyImmutableList(),
                    amountFieldValue = TextFieldValue("5"),
                    amountType = AmountType.Count,
                    isProductDropdownEnabled = false,
                    product = null,
                    availableProducts = emptyImmutableList(),
                    isShopDropdownEnabled = false,
                    shop = null,
                    availableShops = emptyImmutableList()
                ),
            )
        }
    }
}