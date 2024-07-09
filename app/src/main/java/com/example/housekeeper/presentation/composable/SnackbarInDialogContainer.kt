package com.example.housekeeper.presentation.composable

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.housekeeper.presentation.UserMessage
import com.example.housekeeper.presentation.utils.CustomSnackbar
import com.example.housekeeper.presentation.utils.toCustomSnackbarVisuals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SnackbarInDialogContainer(
    message: UserMessage,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val snackbarData = remember {
        message.toCustomSnackbarVisuals(context)
    }
    val animatableAlpha = remember { Animatable(0f) }

    val dur = getDuration(snackbarData.duration)
    if (dur != Long.MAX_VALUE) {
        LaunchedEffect(snackbarData) {
            val animationSpec = tween<Float>(300)
            animatableAlpha.animateTo(1f, animationSpec)
            delay(dur)
            animatableAlpha.animateTo(0f, animationSpec)
            onDismiss()
        }
    }

    val popupPosProvider by imeMonitor(context)
    Popup(
        popupPositionProvider = popupPosProvider,
        properties = PopupProperties(clippingEnabled = false),
    ) {
        CustomSnackbar(
            snackbarData,
            Modifier.graphicsLayer { alpha = animatableAlpha.value },
        )
    }
}


@Composable
private fun getDuration(duration: SnackbarDuration): Long {
    val accessibilityManager = LocalAccessibilityManager.current
    return remember(duration, accessibilityManager) {
        val orig = when (duration) {
            SnackbarDuration.Short -> 4000L
            SnackbarDuration.Long -> 10000L
            SnackbarDuration.Indefinite -> Long.MAX_VALUE
        }
        accessibilityManager?.calculateRecommendedTimeoutMillis(
            orig, containsIcons = true, containsText = true, containsControls = false
        ) ?: orig
    }
}

/**
 * Monitors the size of the IME (software keyboard) and provides an updating
 * PopupPositionProvider.
 */
@Composable
private fun imeMonitor(context: Context): State<PopupPositionProvider> {
    val provider = remember { mutableStateOf(ImePopupPositionProvider()) }
    val decorView = remember(context) { context.getActivity()?.window?.decorView }
    if (decorView != null) {
        val ime = remember { WindowInsetsCompat.Type.ime() }
        val bottom = remember { MutableStateFlow(0) }
        LaunchedEffect(Unit) {
            while (true) {
                bottom.value = ViewCompat.getRootWindowInsets(decorView)?.getInsets(ime)?.bottom ?: 0
                delay(33)
            }
        }
        LaunchedEffect(Unit) {
            bottom.collect { provider.value = ImePopupPositionProvider() }
        }
    }
    return provider
}

/**
 * Places the popup at the bottom of the screen but above the keyboard.
 * This assumes that the anchor for the popup is in the middle of the screen.
 */
private class ImePopupPositionProvider: PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect, windowSize: IntSize,
        layoutDirection: LayoutDirection, popupContentSize: IntSize
    ) = IntOffset(
        anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2,
        anchorBounds.bottom,
    )
}


private fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}