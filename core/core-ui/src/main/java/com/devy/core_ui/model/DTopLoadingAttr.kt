package com.devy.core_ui.model

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.theme.CoreTheme

data class DTopLoadingAttrData(
    val background: Color,
    val visibleText: Boolean = true,
    val text: String? = null,
    val textColor: Color? = null,
    val textStyle: TextStyle? = null,
    val padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
)

@SuppressLint("ComposableNaming")
@Composable
fun DTopLoadingAttr(
    background: Color = CoreTheme.colors.surface.copy(alpha = 0.5f),
    visibleText: Boolean = true,
    text: String? = null,
    textColor: Color? = null,
    textStyle: TextStyle? = null,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
) = DTopLoadingAttrData(
    background = background,
    visibleText = visibleText,
    text = text ?: stringResource(id = R.string.top_loading_content),
    textColor = textColor ?: CoreTheme.colors.onSurface0,
    textStyle = textStyle ?: CoreTheme.typo.body,
    padding = padding,
)