package com.devy.core_ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.component.local.LocalDContentItemColor
import com.devy.core_ui.model.DChipSelectType
import com.devy.core_ui.model.DContentActionUiModel
import com.devy.core_ui.model.DContentItemUiModel
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.conditional
import com.devy.core_ui.utils.container
import com.devy.core_ui.utils.noRippleClickable
import com.devy.core_ui.utils.noRippleCombinedClickable
import com.devy.core_ui.utils.toContentString

/**
 * Created by JeYHoon on 2025. 10. 3..
 */

private const val CARD_ROUNDED_CORNER = 16

@Composable
fun DContentTitleItem(
    modifier: Modifier = Modifier,
    item: DContentItemUiModel.DTitleUiModel,
    surfaceColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    fillMaxWidth: Boolean = true,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
) {

    val surface = safeSurfaceColor(surfaceColor)
    val border = safeBorderColor(borderColor)

    DContentContainer(
        modifier = modifier,
        surfaceColor = surface,
        borderColor = border,
        fillMaxWidth = fillMaxWidth,
        onClick = onClick,
        onLongClick = onLongClick,
    ) {
        DContentTitle(
            item = item,
            titleTrailingIcon = painterResource(id = R.drawable.ic_next_24)
        )
    }
}

@Composable
fun DContentItem(
    modifier: Modifier = Modifier,
    item: DContentItemUiModel.DContentUiModel,
    surfaceColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    fillMaxWidth: Boolean = true,
    titleTrailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
) {

    val background = safeBackgroundColor(backgroundColor)
    val surface = safeSurfaceColor(surfaceColor)
    val border = safeBorderColor(borderColor)

    DContentContainer(
        modifier = modifier,
        surfaceColor = surface,
        borderColor = border,
        fillMaxWidth = fillMaxWidth,
        onClick = onClick,
        onLongClick = onLongClick,
    ) {
        DContentTitle(
            item = item,
            titleTrailingContent = titleTrailingContent,
            titleTrailingIcon = painterResource(id = R.drawable.ic_next_24)
        )

        item.content?.let { content ->
            Text(
                modifier = Modifier.contentContainer(
                    borderColor = border,
                    backgroundColor = background,
                ),
                text = content,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                style = CoreTheme.typo.title,
                color = CoreTheme.colors.onSurface0,
            )
        }
    }
}

@Composable
fun <T> DContentDropdownItem(
    modifier: Modifier = Modifier,
    item: DContentItemUiModel.DContentDropdownUiModel<T>,
    surfaceColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    fillMaxWidth: Boolean = true,
    autoSelectFold: Boolean = true,
    onSelectedChanged: (T) -> Unit,
    onLongClick: () -> Unit = {},
) {

    val background = safeBackgroundColor(backgroundColor)
    val surface = safeSurfaceColor(surfaceColor)
    val border = safeBorderColor(borderColor)

    var expanded by rememberSaveable { mutableStateOf(false) }


    DContentContainer(
        modifier = modifier,
        surfaceColor = surface,
        borderColor = border,
        fillMaxWidth = fillMaxWidth,
        onClick = { expanded = !expanded },
        onLongClick = onLongClick,
    ) {
        DContentTitle(
            item = item,
            rotate = if (expanded) 180f else 0f,
            titleTrailingIcon = painterResource(id = R.drawable.ic_arrow_down_24),
        )

        AnimatedVisibility(
            visible = expanded || item.selectedTrailing.not()
        ) {
            Column(
                modifier = Modifier.contentContainer(
                    borderColor = border,
                    backgroundColor = background,
                )
            ) {
                if (item.selectedTrailing.not()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = item.displayTextMapper(item.selectedContent).toContentString(),
                            style = CoreTheme.typo.body,
                            color = CoreTheme.colors.onSurface0,
                        )

                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(if (expanded) 180f else 0f),
                            painter = painterResource(id = R.drawable.ic_arrow_down_24)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = expanded
                ) {
                    DVerticalListPicker(
                        items = item.content,
                        onSelectChanged = onSelectedChanged,
                        initialIndex = item.content.indexOf(item.selectedContent).coerceAtLeast(0),
                        displayTextMapper = item.displayTextMapper,
                        onClickItem = { if (autoSelectFold) expanded = false }
                    )
                }
            }
        }
    }
}

@Composable
fun <T, R> DContentDoubleDropdownItem(
    modifier: Modifier = Modifier,
    firstItem: DContentItemUiModel.DContentDropdownUiModel<T>,
    onFirstSelectedChanged: (T) -> Unit,
    secondaryItem: DContentItemUiModel.DContentDropdownUiModel<R>,
    onSecondarySelectedChanged: (R) -> Unit,
    surfaceColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    autoSelectFold: Boolean = true,
    fillMaxWidth: Boolean = true,
) {

    val background = safeBackgroundColor(backgroundColor)
    val surface = safeSurfaceColor(surfaceColor)
    val border = safeBorderColor(borderColor)

    var firstExpanded by rememberSaveable { mutableStateOf(false) }
    var secondaryExpanded by rememberSaveable { mutableStateOf(false) }


    DContentContainer(
        modifier = modifier,
        surfaceColor = surface,
        borderColor = border,
        fillMaxWidth = fillMaxWidth,
        onClick = {},
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .noRippleClickable { firstExpanded = !firstExpanded },
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DContentTitle(
                    item = firstItem,
                    rotate = if (firstExpanded) 180f else 0f,
                    titleTrailingIcon = painterResource(id = R.drawable.ic_arrow_down_24),
                )

                AnimatedVisibility(
                    visible = firstExpanded || firstItem.selectedTrailing.not()
                ) {
                    Column(
                        modifier = Modifier.contentContainer(
                            borderColor = border,
                            backgroundColor = background,
                        )
                    ) {
                        if (firstItem.selectedTrailing.not()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = firstItem.displayTextMapper(firstItem.selectedContent)
                                        .toContentString(),
                                    style = CoreTheme.typo.body,
                                    color = CoreTheme.colors.onSurface0,
                                )

                                Image(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .rotate(if (firstExpanded) 180f else 0f),
                                    painter = painterResource(id = R.drawable.ic_arrow_down_24)
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = firstExpanded
                        ) {
                            DVerticalListPicker(
                                items = firstItem.content,
                                onSelectChanged = onFirstSelectedChanged,
                                initialIndex = firstItem.content.indexOf(firstItem.selectedContent)
                                    .coerceAtLeast(0),
                                displayTextMapper = firstItem.displayTextMapper,
                                onClickItem = { if (autoSelectFold) firstExpanded = false },
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .noRippleClickable { secondaryExpanded = !secondaryExpanded },
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DContentTitle(
                    item = secondaryItem,
                    rotate = if (secondaryExpanded) 180f else 0f,
                    titleTrailingIcon = painterResource(id = R.drawable.ic_arrow_down_24),
                )

                AnimatedVisibility(
                    visible = secondaryExpanded || secondaryItem.selectedTrailing.not()
                ) {
                    Column(
                        modifier = Modifier.contentContainer(
                            borderColor = border,
                            backgroundColor = background,
                        )
                    ) {
                        if (secondaryItem.selectedTrailing.not()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = secondaryItem.displayTextMapper(secondaryItem.selectedContent)
                                        .toContentString(),
                                    style = CoreTheme.typo.body,
                                    color = CoreTheme.colors.onSurface0,
                                )

                                Image(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .rotate(if (secondaryExpanded) 180f else 0f),
                                    painter = painterResource(id = R.drawable.ic_arrow_down_24)
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = secondaryExpanded
                        ) {
                            DVerticalListPicker(
                                items = secondaryItem.content,
                                onSelectChanged = onSecondarySelectedChanged,
                                initialIndex = secondaryItem.content.indexOf(secondaryItem.selectedContent)
                                    .coerceAtLeast(0),
                                displayTextMapper = secondaryItem.displayTextMapper,
                                onClickItem = { if (autoSelectFold) secondaryExpanded = false },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DActionContentItem(
    modifier: Modifier = Modifier,
    item: DContentItemUiModel.DContentUiModel,
    action: DContentActionUiModel? = null,
    surfaceColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    fillMaxWidth: Boolean = true,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
) {

    val surface = safeSurfaceColor(surfaceColor)
    val border = safeBorderColor(borderColor)

    val providedPrimary = LocalDCommonColor.current.primary
    val providedOnSurface = LocalDCommonColor.current.onSurface
    val actionBackground = action?.background ?: providedPrimary.copy(0.5f)
    val actionColor = action?.color ?: providedOnSurface.primary

    DContentContainer(
        modifier = modifier,
        surfaceColor = surface,
        borderColor = border,
        fillMaxWidth = fillMaxWidth,
        onClick = onClick,
        onLongClick = onLongClick,
    ) {
        DContentTitle(
            item = item,
        ) {
            action?.let {
                Row(
                    modifier = Modifier
                        .noRippleClickable { action.onClick() }
                        .background(
                            color = actionBackground,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    action.icon?.let { icon ->
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = icon,
                            color = actionColor,
                        )
                    }

                    Text(
                        text = action.text,
                        style = CoreTheme.typo.caption,
                        color = actionColor
                    )
                }
            }
        }

        item.content?.let { content ->
            val message = content.ifBlank { stringResource(id = R.string.is_empty) }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                style = CoreTheme.typo.title,
                color = if (content.isNotEmpty()) CoreTheme.colors.onSurface0 else CoreTheme.colors.onSurface60,
            )
        }
    }
}

@Composable
fun DContentInputItem(
    modifier: Modifier = Modifier,
    item: DContentItemUiModel.DContentInputUiModel,
    surfaceColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    placeholderColor: Color = CoreTheme.colors.onSurface40,
    fillMaxWidth: Boolean = true,
    minHeight: Dp = 0.dp,
    textAlign: TextAlign = TextAlign.Unspecified,
    contentAlignment: Alignment = Alignment.CenterStart,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {

    val background = safeBackgroundColor(backgroundColor)
    val surface = safeSurfaceColor(surfaceColor)
    val border = safeBorderColor(borderColor)

    DContentContainer(
        modifier = modifier,
        surfaceColor = surface,
        borderColor = border,
        fillMaxWidth = fillMaxWidth,
        onClick = { },
    ) {
        DContentTitle(
            item = item,
            titleTrailingIcon = painterResource(id = R.drawable.ic_clear_24),
            onClick = {
                onValueChange("")
            }
        )

        DTextField(
            modifier = Modifier
                .contentContainer(
                    borderColor = border,
                    backgroundColor = background,
                )
                .heightIn(min = minHeight)
                .focusRequester(focusRequester),
            content = item.content.toContentString(),
            placeholder = item.placeholder?.toContentString(),
            placeholderColor = placeholderColor,
            onValueChange = onValueChange,
            contentAlignment = contentAlignment,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textAlign = textAlign,
        )
    }
}

@Composable
fun DContentOnOffItem(
    modifier: Modifier = Modifier,
    item: DContentItemUiModel.DContentOnOffUiModel,
    surfaceColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    fillMaxWidth: Boolean = true,
    onClick: (isOn: Boolean) -> Unit,
) {
    val surface = safeSurfaceColor(surfaceColor)
    val border = safeBorderColor(borderColor)

    val providedPrimary = LocalDCommonColor.current.primary

    fun selectType(isOn: Boolean) = if (isOn) DChipSelectType.Solid else DChipSelectType.Stroke

    @Composable
    fun color(isOn: Boolean) = if (isOn) providedPrimary else CoreTheme.colors.onSurface50

    @Composable
    fun textColor(isOn: Boolean) =
        if (isOn) CoreTheme.colors.onSurface0 else CoreTheme.colors.onSurface50

    @Composable
    fun style(isOn: Boolean) = if (isOn) CoreTheme.typo.bodyB else CoreTheme.typo.body

    DContentContainer(
        modifier = modifier,
        surfaceColor = surface,
        borderColor = border,
        fillMaxWidth = fillMaxWidth,
        onClick = {},
    ) {
        DContentTitle(
            item = item,
        )

        DOnOffSelector(
            modifier = Modifier.fillMaxWidth(),
            isOn = item.isOn,
            onSelected = onClick
        )
    }
}

@Composable
fun DContentContainer(
    modifier: Modifier,
    surfaceColor: Color,
    borderColor: Color,
    fillMaxWidth: Boolean = true,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {

    Column(
        modifier = modifier
            .conditional(fillMaxWidth) { fillMaxWidth() }
            .container(
                background = surfaceColor,
                borderColor = borderColor,
            )
            .noRippleCombinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        content()
    }
}

@Composable
fun DContentTitle(
    item: DContentItemUiModel,
    onClick: () -> Unit = {},
    rotate: Float = 0f,
    titleTrailingContent: (@Composable () -> Unit)? = null,
    titleTrailingIcon: Painter = painterResource(id = R.drawable.ic_next_24),
) {

    DContentTitle(
        item = item,
    ) {
        titleTrailingContent?.let { it() }

        item.titleTrailingContent?.toContentString()?.let { trailingContent ->
            Text(
                text = trailingContent,
                style = CoreTheme.typo.body,
                color = CoreTheme.colors.onSurface0,
            )
        }

        if (item.showTitleTrailingIcon) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotate)
                    .noRippleClickable { onClick() },
                painter = titleTrailingIcon,
            )
        }
    }
}

@Composable
fun DContentTitle(
    item: DContentItemUiModel,
    trailingContent: @Composable RowScope.() -> Unit,
) {

    val providedPrimary = LocalDCommonColor.current.primary
    val primaryColor =
        if (providedPrimary != Color.Unspecified) providedPrimary else CoreTheme.colors.primary

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.itemTitle.toContentString(),
                style = CoreTheme.typo.bodyB,
                color = CoreTheme.colors.onSurface0,
            )

            if (item.itemRequired) {
                Text(
                    text = "*",
                    style = CoreTheme.typo.footnote,
                    color = primaryColor
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            trailingContent()
        }
        item.itemDescription?.let { description ->
            Text(
                modifier = Modifier,
                text = description.toContentString(),
                style = CoreTheme.typo.footnote,
                color = CoreTheme.colors.onSurface60,
            )
        }
    }
}

@Composable
fun Modifier.contentContainer(
    borderColor: Color,
    backgroundColor: Color = CoreTheme.colors.background,
) = this
    .container(
        background = backgroundColor,
        borderColor = borderColor,
        padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    )
    .fillMaxWidth()

@Composable
private fun safeBackgroundColor(color: Color) = safeColor(
    color = color,
    localColor = LocalDContentItemColor.current.background,
    defaultColor = CoreTheme.colors.background
)

@Composable
private fun safeSurfaceColor(color: Color) = safeColor(
    color = color,
    localColor = LocalDContentItemColor.current.surface.copy(alpha = 0.5f),
    defaultColor = CoreTheme.colors.surface
)

@Composable
private fun safeBorderColor(color: Color) = safeColor(
    color = color,
    localColor = LocalDContentItemColor.current.border,
    defaultColor = CoreTheme.colors.border
)

@Composable
private fun safeColor(
    color: Color,
    localColor: Color,
    defaultColor: Color,
) = when {
    color != Color.Unspecified -> color
    localColor != Color.Unspecified -> localColor
    else -> defaultColor
}
