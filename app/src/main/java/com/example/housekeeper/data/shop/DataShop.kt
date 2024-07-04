package com.example.housekeeper.data.shop

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop")
data class DataShop(
    @PrimaryKey val name: String,
)