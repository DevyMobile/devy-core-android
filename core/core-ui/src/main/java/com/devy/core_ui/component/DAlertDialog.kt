package com.devy.core_ui.component

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.devy.core_ui.R
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.orProvided
import com.devy.core_ui.utils.takeOr

/**
 * Created by JeYHoon on 2025. 10. 5..
 */

@Composable
fun DAlertDialog(
    @StringRes title: Int,
    @StringRes content: Int,
    @StringRes positiveText: Int = R.string.confirm,
    @StringRes negativeText: Int = R.string.cancel,
    color: Color? = null,
    onDismiss: () -> Unit,
    onDismissRequest: () -> Unit = {},
    onPositive: () -> Unit,
    onNegative: () -> Unit = {},
) {
    DAlertDialog(
        title = stringResource(id = title),
        content = stringResource(id = content),
        positiveText = stringResource(id = positiveText),
        negativeText = stringResource(id = negativeText),
        color = color,
        onDismiss = onDismiss,
        onDismissRequest = onDismissRequest,
        onPositive = onPositive,
        onNegative = onNegative
    )
}

@Composable
fun DAlertDialog(
    title: String,
    content: String,
    positiveText: String = stringResource(id = R.string.confirm),
    negativeText: String = stringResource(id = R.string.cancel),
    color: Color? = null,
    onDismiss: () -> Unit,
    onDismissRequest: () -> Unit = {},
    onPositive: () -> Unit,
    onNegative: () -> Unit = {},
) {

    val providedColors = LocalDCommonColor.current
    val providedSurface = providedColors.surface
    val providedOnSurface = providedColors.onSurface.primary
    val providedPrimary = providedColors.primary

    val surface = providedSurface.takeOr(CoreTheme.colors.surface)
    val onSurface = providedOnSurface.takeOr(CoreTheme.colors.onSurface0)
    val primary = color.orProvided(
        providedColor = providedPrimary,
        defaultColor = CoreTheme.colors.primary
    )

    AlertDialog(
        containerColor = surface,
        titleContentColor = onSurface,
        textContentColor = onSurface,
        onDismissRequest = {
            // 다이얼로그 바깥 영역 클릭 또는 뒤로가기 버튼 시 호출
            // 여기서는 다이얼로그를 숨김
            onDismissRequest()
            onDismiss()
        },
        title = {
            Text(
                text = title,
                style = CoreTheme.typo.titleB,
            )
        },
        text = {
            Text(
                text = content,
                style = CoreTheme.typo.body,
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onPositive()
                },
                colors = buttonColors(
                    containerColor = primary,
                    contentColor = onSurface,
                )
            ) {
                Text(
                    text = positiveText,
                    style = CoreTheme.typo.body,
                )
            }
        },
        dismissButton = {
            TextButton(
                colors = buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = primary,
                ),
                onClick = {
                    onDismiss()
                    onNegative()
                }
            ) {
                Text(
                    text = negativeText,
                    style = CoreTheme.typo.body,
                )
            }
        }
    )
}