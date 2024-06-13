package com.example.housekeeper.presentation.spending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.housekeeper.presentation.spending.SpendingUIEvent.AddSpend

@Composable
fun SpendingScreen(
    modifier: Modifier,
    spendingViewModel: SpendingViewModel,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = { AddSpendButton { spendingViewModel.accept(AddSpend) } },
    ) { innerPadding ->
        val state = spendingViewModel.state.collectAsState()

        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Spending()
            }
        }
    }
}

@Composable
private fun Spending() {
}

@Composable
private fun AddSpendButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
    ) {
        Icon(Icons.Filled.Add, "add spending")
    }
}