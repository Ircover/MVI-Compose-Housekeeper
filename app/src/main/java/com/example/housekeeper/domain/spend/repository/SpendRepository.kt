package com.example.housekeeper.domain.spend.repository

import com.example.housekeeper.domain.spend.Spend

interface SpendRepository {
    suspend fun isSpendExists(id: Long): Boolean
    suspend fun save(spend: Spend): Result<Spend>
}