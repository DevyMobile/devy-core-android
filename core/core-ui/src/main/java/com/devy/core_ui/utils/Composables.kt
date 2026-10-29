package com.devy.core_ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

fun Context.getComponentActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getComponentActivity()
    else -> null
}

@Composable
inline fun <reified VM : ViewModel> sharedViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory? = null
): VM = viewModel(
    VM::class.java,
    checkNotNull(LocalContext.current.getComponentActivity()),
    key,
    factory
)

@Composable
fun LifecycleEventListener(event: (Lifecycle.Event) -> Unit) {
    val eventHandler by rememberUpdatedState(newValue = event)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            eventHandler(event)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.container(
    background: Color,
    borderWidth: Dp = 1.dp,
    borderColor: Color = CoreTheme.colors.border,
    roundedCorner: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(16.dp),
): Modifier = this.borderBackground(
    background = background,
    borderWidth = borderWidth,
    borderColor = borderColor,
    roundedCorner = roundedCorner,
    padding = padding
)

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.borderBackground(
    background: Color,
    borderWidth: Dp = 1.dp,
    borderColor: Color,
    roundedCorner: Dp,
    padding: PaddingValues,
): Modifier = this
    .clip(RoundedCornerShape(roundedCorner))
    .background(background)
    .border(
        border = BorderStroke(
            width = borderWidth,
            borderColor
        ),
        shape = RoundedCornerShape(roundedCorner)
    )
    .padding(padding)

@Composable
fun Any.toContentString(): String {
    return when (this) {
        is String -> this
        is Int -> runCatching { stringResource(id = this) }.getOrDefault(this.toString())
        else -> this.toString()
    }
}

@Composable
fun Modifier.clipAndBorder(
    shape: RoundedCornerShape,
    background: Color,
    width: Dp = 1.dp,
    border: Color = Color.Unspecified,
): Modifier {

    val providedBorder = LocalDCommonColor.current.border
    val safeBorder = border.takeOr(providedBorder)

    return then(this)
        .clip(shape)
        .background(background)
        .border(
            width = width,
            color = safeBorder,
            shape = shape,
        )
}

@Composable
fun Color?.orProvided(
    providedColor: Color,
    defaultColor: Color = Color.Unspecified,
): Color = when {
    this != null -> this
    providedColor != Color.Unspecified -> providedColor
    else -> defaultColor
}

@Composable
fun Color.takeOr(default: Color): Color = when {
    this != Color.Unspecified -> this
    else -> default
}

@Composable
fun Color.takeOr(default: Color, themeColor: Color): Color = this.takeOr(default).takeOr(themeColor)