package com.example.housekeeper.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.housekeeper.R
import com.example.housekeeper.presentation.utils.mediumRoundedCornerShape
import com.example.housekeeper.presentation.utils.paddingSmall
import com.example.housekeeper.ui.theme.HousekeeperTheme

@Composable
fun DatePickerDialog(
    initialDate: Long,
    onDismissRequest: () -> Unit,
    onOkClick: (Long) -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        DatePickerDialogContent(initialDate, onOkClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogContent(
    initialDate: Long,
    onOkClick: (Long) -> Unit = { },
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = mediumRoundedCornerShape(),
    ) {
        val state = rememberDatePickerState(initialDate)
        DatePicker(state)
        Button(
            onClick = {
                state.selectedDateMillis?.let { result ->
                    onOkClick(result)
                }
            },
            modifier = Modifier
                .paddingSmall()
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.ok))
        }
    }
}

@Preview
@Composable
private fun PreviewDatePickerDialog() {
    HousekeeperTheme {
        Surface {
            DatePickerDialogContent(
                initialDate = 1L,
            )
        }
    }
}