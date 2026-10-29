package com.devy.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.takeOr

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Composable
fun DOutlinedTextField(
    modifier: Modifier = Modifier,
    content: String,
    placeholder: String? = null,
    placeholderColor: Color = CoreTheme.colors.onSurface40,
    textFieldColors: TextFieldColors = CoreOutlinedTextFieldDefaults.colors(),
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.windowInsetsPadding(WindowInsets.ime),
        textStyle = CoreTheme.typo.body,
        value = content,
        onValueChange = onValueChange,
        colors = textFieldColors,
        placeholder = {
            placeholder?.let {
                PlaceHolder(placeholder, placeholderColor)
            }
        })
}

@Composable
fun DTextField(
    modifier: Modifier = Modifier,
    content: String,
    placeholder: String? = null,
    placeholderColor: Color = CoreTheme.colors.onSurface40,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    textColor: Color = Color.Unspecified,
    textStyle: TextStyle = CoreTheme.typo.body,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    contentAlignment: Alignment = Alignment.CenterStart,
    textAlign: TextAlign = TextAlign.Unspecified,
    onValueChange: (String) -> Unit,
) {

    val providedOnSurface = LocalDCommonColor.current.onSurface.primary

    BasicTextField(
        value = content,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        textStyle = textStyle
            .copy(
                color = textColor.takeOr(providedOnSurface, CoreTheme.colors.onSurface0),
                textAlign = textAlign,
            ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(providedOnSurface.takeOr(CoreTheme.colors.onSurface0)),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = contentAlignment,
            ) {
                if (content.isEmpty()) {
                    placeholder?.let {
                        PlaceHolder(placeholder, placeholderColor, textStyle)
                    }
                }
                innerTextField()
            }
        })
}

@Composable
private fun PlaceHolder(
    placeholder: String,
    placeholderColor: Color,
    textStyle: TextStyle = CoreTheme.typo.body,
) {
    Text(
        text = placeholder, style = textStyle, color = placeholderColor
    )
}