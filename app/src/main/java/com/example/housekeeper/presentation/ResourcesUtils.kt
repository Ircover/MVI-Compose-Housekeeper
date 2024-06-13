package com.example.housekeeper.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.housekeeper.R

@Composable
fun Modifier.paddingSmall() = this.padding(dimensionResource(R.dimen.padding_small))

@Composable
fun Arrangement.spacedVerticallyByDefault() = this.spacedBy(dimensionResource(R.dimen.spacing_vertical_default))