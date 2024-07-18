package com.example.housekeeper.presentation.spend_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.housekeeper.R
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.presentation.UserMessage
import com.example.housekeeper.presentation.composable.AddProductDialog
import com.example.housekeeper.presentation.composable.AddShopDialog
import com.example.housekeeper.presentation.composable.CustomDropdownMenu
import com.example.housekeeper.presentation.composable.CustomTextField
import com.example.housekeeper.presentation.composable.DatePickerDialog
import com.example.housekeeper.presentation.composable.DateTextField
import com.example.housekeeper.presentation.composable.PriceTextField
import com.example.housekeeper.presentation.composable.RadioTextButton
import com.example.housekeeper.presentation.emptyImmutableList
import com.example.housekeeper.presentation.utils.CustomSnackbarHost
import com.example.housekeeper.presentation.utils.collectState
import com.example.housekeeper.presentation.utils.paddingSmall
import com.example.housekeeper.presentation.utils.show
import com.example.housekeeper.presentation.utils.spacedVerticallyByDefault
import com.example.housekeeper.ui.theme.HousekeeperTheme
import com.example.housekeeper.ui.theme.LocalCustomColorsPalette
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
        val onDateChanged = remember {
            spendCardViewModel.accept { date: Long ->
                SpendCardUIEvent.DateChanged(date)
            }
        }
        val onCommentChanged = remember {
            spendCardViewModel.accept { newComment: TextFieldValue ->
                SpendCardUIEvent.CommentChanged(newComment)
            }
        }
        val onSaveClick = remember {
            spendCardViewModel.accept {
                SpendCardUIEvent.SaveClick
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
            onDateChanged,
            onCommentChanged,
            onSaveClick,
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
    onDateChanged: (Long) -> Unit,
    onCommentChanged: (TextFieldValue) -> Unit,
    onSaveClick: () -> Unit,
) {
    val context = LocalContext.current
    val state = spendCardViewModel.collectState().value
    var isAddProductDialogVisible by remember { mutableStateOf(false) }
    var isAddProductDialogLoading by remember { mutableStateOf(false) }
    var isAddShopDialogVisible by remember { mutableStateOf(false) }
    var isAddShopDialogLoading by remember { mutableStateOf(false) }
    var isDatePickerDialogVisible by remember { mutableStateOf(false) }
    val onDateClick: () -> Unit by rememberUpdatedState { isDatePickerDialogVisible = true }
    val coroutineScope = rememberCoroutineScope()

    var productDialogMessage: UserMessage? by remember { mutableStateOf(null) }
    var shopDialogMessage: UserMessage? by remember { mutableStateOf(null) }

    spendCardViewModel.collectSideEffect {
        when (it) {
            is SpendCardSideEffect.ShowMessage -> {
                snackbarHostState.show(context, coroutineScope, it.message)
            }
            is SpendCardSideEffect.ShowProductDialogMessage -> {
                productDialogMessage = it.message
            }
            is SpendCardSideEffect.ShowShopDialogMessage -> {
                shopDialogMessage = it.message
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
            onDateClick = onDateClick,
            onCommentChanged = onCommentChanged,
            onSaveClick = onSaveClick,
        )
        if (isAddProductDialogVisible) {
            AddProductDialog(
                isLoading = isAddProductDialogLoading,
                message = productDialogMessage,
                onMessageDismiss = { productDialogMessage = null },
                onProductAdd = spendCardViewModel.accept { name ->
                    SpendCardUIEvent.AddProduct(name)
                },
                onDismissRequest = { isAddProductDialogVisible = false },
            )
        }
        if (isAddShopDialogVisible) {
            AddShopDialog(
                isLoading = isAddShopDialogLoading,
                message = shopDialogMessage,
                onMessageDismiss = { shopDialogMessage = null },
                onShopAdd = spendCardViewModel.accept { name ->
                    SpendCardUIEvent.AddShop(name)
                },
                onDismissRequest = { isAddShopDialogVisible = false },
            )
        }
        if (isDatePickerDialogVisible) {
            DatePickerDialog(
                initialDate = state.dateMillis,
                onDismissRequest = { isDatePickerDialogVisible = false },
                onOkClick = {
                    isDatePickerDialogVisible = false
                    onDateChanged(it)
                }
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
    onDateClick: () -> Unit = { },
    onCommentChanged: (TextFieldValue) -> Unit = { },
    onSaveClick: () -> Unit = { },
) {
    val firstColumnWidthFraction = 0.25f
    Column {
        Column(
            verticalArrangement = Arrangement.spacedVerticallyByDefault(),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            //Дата
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.date),
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(firstColumnWidthFraction),
                )
                DateTextField(
                    date = state.dateString,
                    onClick = onDateClick,
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(),
                )
            }
            //Цена
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.price),
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(firstColumnWidthFraction),
                )
                Column {
                    Row {
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
                    if (state.isEmptyPriceErrorVisible) {
                        Text(
                            text = stringResource(R.string.error_empty_price),
                            color = LocalCustomColorsPalette.current.error,
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
            }
            //Количество
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.amount),
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(firstColumnWidthFraction),
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomTextField(
                        value = state.amountFieldValue,
                        onValueChange = onAmountTextChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .paddingSmall(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    if (state.isEmptyAmountErrorVisible) {
                        Text(
                            text = stringResource(R.string.error_empty_amount),
                            color = LocalCustomColorsPalette.current.error,
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                        )
                    }
                    Row {
                        RadioTextButton(
                            isSelected = state.amountType == AmountType.Count,
                            title = stringResource(R.string.radio_count),
                            modifier = Modifier.weight(1f),
                            onClick = { onAmountTypeChanged(AmountType.Count) },
                        )
                        RadioTextButton(
                            isSelected = state.amountType == AmountType.Weight,
                            title = stringResource(R.string.radio_weigth),
                            modifier = Modifier.weight(1f),
                            onClick = { onAmountTypeChanged(AmountType.Weight) },
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
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(firstColumnWidthFraction),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min)
                ) {
                    CustomDropdownMenu(
                        modifier = Modifier
                            .paddingSmall(),
                        selectedItem = state.product,
                        items = state.availableProducts,
                        itemFormatter = {
                            it?.name ?: stringResource(R.string.product_empty_placeholder)
                        },
                        onItemChanged = onProductChanged,
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
                                text = product?.name
                                    ?: stringResource(R.string.product_empty_placeholder),
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
                    if (state.isEmptyProductErrorVisible) {
                        Text(
                            text = stringResource(R.string.error_empty_product),
                            color = LocalCustomColorsPalette.current.error,
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
                IconButton(
                    modifier = Modifier.paddingSmall(),
                    onClick = onProductAddClick,
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
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(firstColumnWidthFraction),
                )
                CustomDropdownMenu(
                    modifier = Modifier
                        .weight(1f)
                        .paddingSmall(),
                    selectedItem = state.shop,
                    items = state.availableShops,
                    itemFormatter = { it?.name ?: stringResource(R.string.shop_empty_placeholder) },
                    onItemChanged = onShopChanged,
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
                                onShopDeleteClick(shop)
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = null)
                            }
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.paddingSmall(),
                    onClick = onShopAddClick,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = "add spending shop",
                    )
                }
            }
            //Комментарий
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.comment),
                    modifier = Modifier
                        .paddingSmall()
                        .fillMaxWidth(firstColumnWidthFraction),
                )
                CustomTextField(
                    value = state.comment,
                    onValueChange = onCommentChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingSmall(),
                )
            }
        }
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .paddingSmall()
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Preview
@Composable
fun PreviewSpendCardScreen_Default() {
    PreviewSpendCardScreen(
        showAllErrors = false,
    )
}

@Preview
@Composable
fun PreviewSpendCardScreen_Errors() {
    PreviewSpendCardScreen(
        showAllErrors = true,
    )
}

@Composable
private fun PreviewSpendCardScreen(showAllErrors: Boolean) {
    HousekeeperTheme {
        Surface {
            RenderSpendCardViewModel(
                SpendCardState(
                    dateMillis = 0L,
                    dateString = "01.01.2024",
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
                    availableShops = emptyImmutableList(),
                    comment = TextFieldValue(),

                    isEmptyPriceErrorVisible = showAllErrors,
                    isEmptyAmountErrorVisible = showAllErrors,
                    isEmptyProductErrorVisible = showAllErrors,
                ),
            )
        }
    }
}