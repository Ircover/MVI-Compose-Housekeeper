package com.example.housekeeper.presentation.utils

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.orbitmvi.orbit.ContainerHost

@Composable
fun <T : Any> ContainerHost<T, *>.collectState() = container.stateFlow.collectAsStateWithLifecycle()