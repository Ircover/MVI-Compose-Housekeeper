package com.example.housekeeper.presentation.spending

import androidx.lifecycle.ViewModel
import com.example.housekeeper.presentation.NavManager
import com.example.housekeeper.presentation.spending.SpendingUIEvent.AddSpend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpendingViewModel(
    private val navManager: NavManager,
) : ViewModel() {
    private val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()

    fun accept(event: SpendingUIEvent) {
        when (event) {
            AddSpend -> {
                navManager.openSpendCard()
            }
        }
    }
}

private fun initState() = SpendingState(
    counter = 0,
)