package com.example.housekeeper.data.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class DataProduct(
    @PrimaryKey val name: String,
)