package com.example.housekeeper.data.product

import com.example.housekeeper.domain.product.Product

fun DataProduct.toProduct() = Product(name)
fun Product.toDataProduct() = DataProduct(name)