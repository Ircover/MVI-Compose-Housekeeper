package com.example.housekeeper.presentation.composable

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CustomTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.housekeeper.domain.Currency

@Composable
fun PriceTextField(
    modifier: Modifier,
    price: String,
    currency: Currency,
    onPriceChanged: (String) -> Unit,
) {
    CustomTextField(
        value = price,
        onValueChange = onPriceChanged,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = CurrencyTransformation(currency),
        singleLine = true,
    )
}

private class CurrencyTransformation(val currency: Currency) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            AnnotatedString("${text.text} ${currency.sign}"),
            AdditionalCharactersOffsetMapping(text.length),
        )
    }
}

private class AdditionalCharactersOffsetMapping(
    val textLength: Int,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int) = offset

    override fun transformedToOriginal(offset: Int) =
        if (textLength > offset) offset else textLength
}