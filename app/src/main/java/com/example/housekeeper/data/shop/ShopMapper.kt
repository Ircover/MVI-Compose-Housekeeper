package com.example.housekeeper.data.shop

import com.example.housekeeper.domain.shop.Shop

fun DataShop.toShop() = Shop(name)
fun Shop.toDataShop() = DataShop(name)
