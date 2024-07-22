package com.example.housekeeper.domain.spend.usecase

import com.example.housekeeper.domain.EMPTY_ID
import com.example.housekeeper.domain.spend.Spend
import com.example.housekeeper.domain.spend.repository.SpendRepository

class SaveSpendUsecase(
    private val spendRepository: SpendRepository,
) {
    suspend operator fun invoke(spend: Spend): SaveSpendResult {
        return if (spend.id != EMPTY_ID && !spendRepository.isSpendExists(spend.id)) {
            SaveSpendResult.NotExistingSpendError
        } else {
            val result = spendRepository.save(spend)
            if (result.isSuccess) {
                SaveSpendResult.Success
            } else {
                SaveSpendResult.UnknownError(result.exceptionOrNull())
            }
        }
    }
}

sealed interface SaveSpendResult {
    data object Success : SaveSpendResult
    data object NotExistingSpendError : SaveSpendResult
    data class UnknownError(val t: Throwable?): SaveSpendResult
}