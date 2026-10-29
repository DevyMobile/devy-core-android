package com.devy.core_ui.component.bottomsheet

import android.view.Window
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT
import com.devy.core_ui.component.GradientOverlay
import com.devy.core_ui.component.GradientType
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.model.DBottomSheetListItemUiModel
import com.devy.core_ui.model.DBottomSheetListTextStyle
import com.devy.core_ui.model.DBottomSheetStyle
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable
import com.devy.core_ui.utils.orProvided
import com.devy.core_ui.utils.takeOr
import com.devy.core_ui.utils.toDp
import com.devy.core_ui.utils.toPx
import kotlin.math.max

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Composable
fun DBottomSheetModal(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DBottomSheetStyle = DBottomSheetStyle(
        backgroundColor = Color.Unspecified,
        titleStyle = CoreTheme.typo.titleB,
        titleColor = Color.Unspecified,
    ),
    topPadding: Dp = 60.dp,
    @StringRes title: Int,
    button: BottomSheetModalButton? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    DBottomSheetModal(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        style = style,
        topPadding = topPadding,
        title = stringResource(id = title),
        button = button,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DBottomSheetModal(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DBottomSheetStyle = DBottomSheetStyle(
        backgroundColor = Color.Unspecified,
        titleStyle = CoreTheme.typo.titleB,
        titleColor = Color.Unspecified,
    ),
    topPadding: Dp = 60.dp,
    title: CharSequence? = null,
    button: BottomSheetModalButton? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val providedBackground = LocalDCommonColor.current.background

    ModalBottomSheet(
        modifier = Modifier.padding(top = topPadding),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = style.backgroundColor.takeOr(
            providedBackground,
            CoreTheme.colors.background
        ),
        scrimColor = Color.Black.copy(alpha = 0.75f),
    ) {
        InsideBottomSheet(modifier, style, title, button, content)
    }
}

@Composable
private fun InsideBottomSheet(
    modifier: Modifier = Modifier,
    style: DBottomSheetStyle = DBottomSheetStyle(
        backgroundColor = Color.Unspecified,
        titleStyle = CoreTheme.typo.titleB,
        titleColor = Color.Unspecified,
    ),
    title: CharSequence? = null,
    button: BottomSheetModalButton? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    val providedOnSurface = LocalDCommonColor.current.onSurface.primary

    val contentScrollState = rememberScrollState()

    val showBottomGradient by remember { derivedStateOf { contentScrollState.canScrollForward } }
    val showTopGradient by remember { derivedStateOf { contentScrollState.canScrollBackward } }

    Column(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(-(24.dp)),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = if (button == null) 0.dp else 24.dp)
                .weight(1f, fill = false)
        ) {
            title?.let {
                BottomSheetModalTitle(
                    title = it,
                    style = style.titleStyle,
                    color = style.titleColor.takeOr(providedOnSurface)
                        .takeOr(CoreTheme.colors.onSurface0)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            Box {
                Column(
                    modifier = Modifier.verticalScroll(contentScrollState)
                ) {
                    content()
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (showTopGradient) {
                    GradientOverlay(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp),
                        type = GradientType.Top
                    )
                }

                if (showBottomGradient) {
                    GradientOverlay(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp),
                        type = GradientType.Bottom
                    )
                }
            }
        }
        button?.let {
            BottomSheetModalButton(button = it)
        }
    }
}

@Composable
fun BottomSheetModalTitle(
    title: CharSequence,
    style: TextStyle,
    color: Color,
) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    if (title is AnnotatedString) {
        Text(
            text = title,
            style = style,
            textAlign = TextAlign.Center,
            modifier = modifier,
            color = color
        )
    } else {
        Text(
            text = title.toString(),
            style = style,
            textAlign = TextAlign.Center,
            modifier = modifier,
            color = color
        )
    }
}

@Composable
fun BottomSheetModalButton(
    button: BottomSheetModalButton,
    padding: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        bottom = 14.dp
    ),
    backgroundColor: Color = Color.Unspecified,
    animateHide: () -> Unit = {},
) {

    val providedBackground = LocalDCommonColor.current.background

    val minHeight = 54.dp.toPx()
    var maxHeight by remember { mutableFloatStateOf(minHeight) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                backgroundColor.takeOr(
                    default = providedBackground,
                    themeColor = CoreTheme.colors.background
                )
            )
            .padding(padding)
    ) {
        val modifier = Modifier
            .heightIn(min = maxHeight.toDp())
            .onSizeChanged { maxHeight = max(it.height.toFloat(), maxHeight) }

        if (button.negativeButton != null) {
            ModalButtonType.DualNegative.ModalButton(
                this,
                modifier,
                button.negativeButton,
                animateHide
            )
            ModalButtonType.DualPositive.ModalButton(
                this,
                modifier,
                button.positiveButton,
                animateHide
            )
        } else {
            ModalButtonType.Single.ModalButton(this, modifier, button.positiveButton, animateHide)
        }
    }
}

@Composable
fun DBottomSheetListItem(
    data: DBottomSheetListItemUiModel,
    textStyle: DBottomSheetListTextStyle = DBottomSheetListTextStyle(
        color = CoreTheme.colors.onSurface0,
        style = CoreTheme.typo.body
    ),
) {
    DBottomSheetListItem(
        text = data.title(),
        textStyle = textStyle,
        onClick = data.onClick,
    )
}

@Composable
fun DBottomSheetListItem(
    @StringRes text: Int,
    textStyle: DBottomSheetListTextStyle = DBottomSheetListTextStyle(
        color = CoreTheme.colors.onSurface0,
        style = CoreTheme.typo.body
    ),
    onClick: () -> Unit,
) {
    DBottomSheetListItem(
        text = stringResource(id = text),
        textStyle = textStyle,
        onClick = onClick
    )
}

@Composable
fun DBottomSheetListItem(
    text: String,
    textStyle: DBottomSheetListTextStyle = DBottomSheetListTextStyle(
        color = CoreTheme.colors.onSurface0,
        style = CoreTheme.typo.body
    ),
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .noRippleClickable { onClick() },
        text = text,
        style = textStyle.style,
        color = textStyle.color,
    )
}

data class BottomSheetModalButton(
    val positiveButton: ButtonInfo,
    val negativeButton: ButtonInfo? = null
)

data class ButtonInfo(
    val text: String = "",
    @StringRes val textResId: Int? = null,
    val enabled: Boolean = true,
    val color: Color? = null,
    val onDisabledClick: () -> Unit = {},
    val autoAnimateHide: Boolean = true,
    val onClick: () -> Unit,
)

enum class ModalButtonType {
    Single,
    Stroke,
    DualPositive,
    DualNegative;

    private val textStyle: TextStyle
        @Composable
        get() = if (this == DualNegative) CoreTheme.typo.body else CoreTheme.typo.bodyB

    private val textColor: Color
        @Composable
        get() = CoreTheme.colors.onSurface0

    private val disabledTextColor: Color
        @Composable
        get() = if (this == DualNegative) CoreTheme.colors.onSurface0 else CoreTheme.colors.onSurface0

    @Composable
    fun ModalButton(
        rowScope: RowScope,
        modifier: Modifier,
        buttonInfo: ButtonInfo,
        animateHide: () -> Unit = {}
    ) =
        Text(
            text = buttonInfo.textResId?.let { stringResource(id = it) } ?: buttonInfo.text,
            modifier = rowScope
                .getModifier(
                    modifier,
                    if (buttonInfo.enabled) buttonInfo.color else CoreTheme.colors.onSurface70,
                    buttonInfo = buttonInfo,
                    onDecoratedClick = {
                        buttonInfo.onClick()
                        if (buttonInfo.autoAnimateHide) animateHide()
                    }
                )
                .padding(16.dp),
            style = textStyle,
            color = if (buttonInfo.enabled) textColor else disabledTextColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    @Composable
    private fun RowScope.getModifier(
        modifier: Modifier,
        color: Color? = null,
        buttonInfo: ButtonInfo,
        onDecoratedClick: () -> Unit,
    ): Modifier {

        val providedPrimary = LocalDCommonColor.current.primary

        return when (this@ModalButtonType) {
            ModalButtonType.Single -> {
                modifier
                    .clip(RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp))
                    .clickable { if (buttonInfo.enabled) onDecoratedClick() else buttonInfo.onDisabledClick() }
                    .composed {
                        background(
                            color.orProvided(providedPrimary).takeOr(CoreTheme.colors.primary),
                            RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp)
                        ).weight(1f)
                    }
            }

            Stroke -> {
                modifier
                    .clip(RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp))
                    .clickable { if (buttonInfo.enabled) onDecoratedClick() else buttonInfo.onDisabledClick() }
                    .composed {
                        border(
                            2.dp,
                            color.orProvided(providedPrimary).takeOr(CoreTheme.colors.primary),
                            RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp)
                        ).weight(1f)
                    }
            }

            ModalButtonType.DualPositive -> {
                modifier
                    .clip(
                        RoundedCornerShape(
                            topEnd = BUTTON_ROUNDED_CORNER.dp,
                            bottomEnd = BUTTON_ROUNDED_CORNER.dp
                        )
                    )
                    .clickable { if (buttonInfo.enabled) onDecoratedClick() else buttonInfo.onDisabledClick() }
                    .composed {
                        background(
                            color.orProvided(providedPrimary).takeOr(CoreTheme.colors.primary),
                            RoundedCornerShape(
                                topEnd = BUTTON_ROUNDED_CORNER.dp,
                                bottomEnd = BUTTON_ROUNDED_CORNER.dp
                            )
                        ).weight(1f)
                    }
            }

            ModalButtonType.DualNegative -> {
                modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = BUTTON_ROUNDED_CORNER.dp,
                            bottomStart = BUTTON_ROUNDED_CORNER.dp
                        )
                    )
                    .clickable { if (buttonInfo.enabled) onDecoratedClick() else buttonInfo.onDisabledClick() }
                    .composed {
                        border(
                            2.dp,
                            color.orProvided(providedPrimary).takeOr(CoreTheme.colors.primary),
                            RoundedCornerShape(
                                topStart = BUTTON_ROUNDED_CORNER.dp,
                                bottomStart = BUTTON_ROUNDED_CORNER.dp
                            )
                        ).weight(1f)
                    }
            }
        }
    }

    companion object {
        private const val BUTTON_ROUNDED_CORNER = 20
    }
}

private fun android.content.Context.findWindow(): Window? {
    var context = this
    while (context is android.content.ContextWrapper) {
        if (context is android.app.Activity) return context.window
        if (context is android.app.Dialog) return context.window // Dialog 내부 Window 캐치
        context = context.baseContext
    }
    return null
}