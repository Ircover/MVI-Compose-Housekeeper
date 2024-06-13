package com.example.housekeeper.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.orbitmvi.orbit.ContainerHost

@Composable
fun <T : Any> ContainerHost<T, *>.collectState() = container.stateFlow.collectAsState()