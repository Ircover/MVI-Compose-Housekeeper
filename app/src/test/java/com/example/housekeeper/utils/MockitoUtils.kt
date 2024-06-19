package com.example.housekeeper.utils

import android.annotation.SuppressLint
import org.mockito.Mockito

@SuppressLint("CheckResult")
fun <T> anyObject(): T {
    Mockito.any<T>()
    return uninitialized()
}
@Suppress("UNCHECKED_CAST")
fun <T> uninitialized(): T = null as T