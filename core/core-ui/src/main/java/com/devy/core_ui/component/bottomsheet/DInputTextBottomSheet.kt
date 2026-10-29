package com.devy.core_ui.component.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.component.CoreOutlinedTextFieldDefaults
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.toContentString

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Composable
fun DInputTextBottomSheet(
    title: Any,
    content: String,
    color: Color? = null,
    onDismissRequest: () -> Unit,
    onClickConfirm: (String) -> Unit,
    onClickCancel: (() -> Unit)? = null,
    confirmButtonText: String = stringResource(id = R.string.save),
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    textFieldColors: TextFieldColors = CoreOutlinedTextFieldDefaults.colors(),
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit = {},
    shape: Shape = RoundedCornerShape(16.dp),
    trailingContent: @Composable (ColumnScope.() -> Unit)? = null,
) {

    val providedPrimary = LocalDCommonColor.current.primary
    val providedBackground = LocalDCommonColor.current.background
    val primary = when {
        color != null -> color
        providedPrimary != Color.Unspecified -> providedPrimary
        else -> CoreTheme.colors.primary
    }

    val safeTextFieldColors = when {
        textFieldColors == CoreOutlinedTextFieldDefaults.colors() -> {
            CoreOutlinedTextFieldDefaults.colors(
                containerColor = providedBackground,
                borderColor = providedPrimary,
            )
        }
        else -> textFieldColors
    }

    val titleContent = title.toContentString()

    var inputText by remember { mutableStateOf(content) }
    LaunchedEffect(content) {
        inputText = content
    }

    DBottomSheetModal(
        title = titleContent,
        onDismissRequest = onDismissRequest,
        button = BottomSheetModalButton(
            positiveButton = ButtonInfo(
                text = confirmButtonText,
                color = primary,
                onClick = {
                    onClickConfirm(inputText)
                    onDismissRequest()
                },
            ),
            negativeButton = ButtonInfo(
                text = stringResource(id = R.string.cancel),
                color = primary,
                onClick = {
                    onClickCancel?.invoke()
                    onDismissRequest()
                },
            )
        )
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (singleLine) 106.dp else 200.dp)
                .padding(horizontal = 16.dp),
            textStyle = CoreTheme.typo.body,
            colors = safeTextFieldColors,
            value = inputText,
            onValueChange = {
                inputText = it
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            singleLine = singleLine,
            shape = shape,
        )


        trailingContent?.let { trailing ->
            Spacer(modifier = Modifier.height(24.dp))
            trailing()
        }
    }
}