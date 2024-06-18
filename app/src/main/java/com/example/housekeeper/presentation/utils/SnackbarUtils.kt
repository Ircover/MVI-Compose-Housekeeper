package com.example.housekeeper.presentation.utils

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.housekeeper.presentation.UserMessage
import com.example.housekeeper.presentation.UserMessageLevel
import com.example.housekeeper.presentation.UserMessageShowDuration
import com.example.housekeeper.ui.theme.HousekeeperTheme

@Composable
fun CustomSnackbarHost(hostState: SnackbarHostState) = SnackbarHost(hostState) { data ->
    val visuals = data.visuals
    if (visuals is CustomSnackbarVisuals) {
        CustomSnackbar(visuals)
    } else {
        Snackbar(snackbarData = data)
    }
}
@Composable
private fun CustomSnackbar(visuals: CustomSnackbarVisuals) {
    Snackbar {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                visuals.icon.icon,
                contentDescription = null,
                tint = visuals.icon.iconTint.color,
            )
            Text(
                visuals.message,
                modifier = Modifier.paddingSmall(),
            )
        }
    }
}

suspend fun SnackbarHostState.show(context: Context, message: UserMessage) {
    showSnackbar(
        CustomSnackbarVisuals(
            message = context.getString(message.textResId),
            duration = when(message.duration) {
                UserMessageShowDuration.Indefinite -> SnackbarDuration.Indefinite
                UserMessageShowDuration.Long -> SnackbarDuration.Long
                UserMessageShowDuration.Short -> SnackbarDuration.Short
            },
            icon = when (message.level) {
                UserMessageLevel.Success -> CustomSnackbarIcon.Success
                UserMessageLevel.Error -> CustomSnackbarIcon.Error
            },
            withDismissAction = true,
            actionLabel = null,
        )
    )
}

private data class CustomSnackbarVisuals(
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val message: String,
    override val withDismissAction: Boolean,
    val icon: CustomSnackbarIcon,
) : SnackbarVisuals

private enum class CustomSnackbarIcon(
    val icon: ImageVector,
    val iconTint: SpecialColor,
) {
    Success(Icons.Filled.CheckCircle, SpecialColor.Success),
    Error(Icons.Filled.Close, SpecialColor.Error),
}

@Preview
@Composable
fun PreviewCustomSnackbar_Success() {
    PreviewCustomSnackbar(CustomSnackbarIcon.Success)
}

@Preview
@Composable
fun PreviewCustomSnackbar_Error() {
    PreviewCustomSnackbar(CustomSnackbarIcon.Error)
}

@Composable
private fun PreviewCustomSnackbar(icon: CustomSnackbarIcon) {
    HousekeeperTheme {
        Surface {
            CustomSnackbar(
                CustomSnackbarVisuals(
                    message = "Test",
                    duration = SnackbarDuration.Short,
                    icon = icon,
                    withDismissAction = true,
                    actionLabel = null,
                )
            )
        }
    }
}