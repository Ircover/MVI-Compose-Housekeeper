package com.example.housekeeper.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.housekeeper.R
import com.example.housekeeper.ui.theme.transparent

@Composable
fun Modifier.paddingSmall() = this.padding(dimensionResource(R.dimen.padding_small))

@Composable
fun Modifier.paddingMedium() = this.padding(dimensionResource(R.dimen.padding_medium))

@Composable
fun Arrangement.spacedVerticallyByDefault() = this.spacedBy(dimensionResource(R.dimen.spacing_vertical_default))

@Composable
fun Modifier.transparentBackground() = this.background(transparent)

@Composable
fun mediumRoundedCornerShape() = RoundedCornerShape(dimensionResource(R.dimen.corner_medium))