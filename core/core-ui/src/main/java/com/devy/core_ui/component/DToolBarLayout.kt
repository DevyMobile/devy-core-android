package com.devy.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.component.local.LocalDToolBarColor
import com.devy.core_ui.model.DBottomButtonInfo
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Composable
fun DToolBarLayout(
    title: String,
    navigationIcon: NavigationIcon = NavigationIcon.None(),
    toolBarColor: Color = CoreTheme.colors.onSurface0,
    visibleDivider: Boolean = false,
    bottomButtonInfo: DBottomButtonInfo? = null,
    visibleBottomGradient: Boolean = true,
    trailingContent: @Composable RowScope.() -> Unit = {},
    content: @Composable BoxScope.(gradientHeight: Dp) -> Unit
) {
    DToolBarLayout(
        title = title,
        navigationIcon = navigationIcon,
        toolBarColor = toolBarColor,
        visibleDivider = visibleDivider,
        bottomButtonInfos = bottomButtonInfo?.let { listOf(it) } ?: emptyList(),
        visibleBottomGradient = visibleBottomGradient,
        trailingContent = trailingContent,
        content = content
    )
}

@Composable
fun DToolBarLayout(
    title: String,
    navigationIcon: NavigationIcon = NavigationIcon.None(),
    toolBarColor: Color = CoreTheme.colors.onSurface0,
    visibleDivider: Boolean = false,
    bottomButtonInfos: List<DBottomButtonInfo>,
    visibleBottomGradient: Boolean = true,
    trailingContent: @Composable RowScope.() -> Unit = {},
    content: @Composable BoxScope.(gradientHeight: Dp) -> Unit
) {
    DToolBarLayout(
        title = title,
        navigationIcon = navigationIcon,
        toolBarColor = toolBarColor,
        visibleDivider = visibleDivider,
        visibleBottomGradient = visibleBottomGradient,
        trailingContent = trailingContent,
        bottomGradientHeight = bottomButtonInfos.gradientHeight(),
        bottomFloatingContent = {
            DBottomButtonContainer(
                visibleBottomGradient = visibleBottomGradient,
                bottomButtonInfos = bottomButtonInfos,
            )
        },
        content = content,
    )
}

@Composable
fun DToolBarLayout(
    title: String,
    navigationIcon: NavigationIcon = NavigationIcon.None(),
    toolBarColor: Color = CoreTheme.colors.onSurface0,
    visibleDivider: Boolean = false,
    visibleBottomGradient: Boolean = true,
    bottomGradientHeight: Dp = BOTTOM_GRADIENT_HEIGHT.dp,
    trailingContent: @Composable RowScope.() -> Unit = {},
    bottomFloatingContent: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.(gradientHeight: Dp) -> Unit
) {

    DToolBarLayout(
        visibleDivider = visibleDivider,
        visibleBottomGradient = visibleBottomGradient,
        bottomGradientHeight = bottomGradientHeight,
        navigationBarContent = {
            DNavigationBar {
                navigationIcon.iconResId?.let { iconResId ->
                    Box(
                        modifier = Modifier
                            .size(TITLE_BAR_HEIGHT.dp)
                            .noRippleClickable {
                                navigationIcon.onClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = null,
                            colorFilter = if (navigationIcon is NavigationIcon.Custom) null else ColorFilter.tint(
                                color = toolBarColor,
                            )
                        )
                    }
                } ?: run {
                    Spacer(modifier = Modifier.width(TITLE_BAR_HEIGHT.dp))
                }

                Box(
                    modifier = Modifier.heightIn(min = TITLE_BAR_HEIGHT.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        text = title,
                        style = CoreTheme.typo.title,
                        color = toolBarColor,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.height(TITLE_BAR_HEIGHT.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    trailingContent()
                }
            }
        },
        bottomFloatingContent = bottomFloatingContent,
        content = content
    )
}

@Composable
fun DToolBarLayout(
    visibleDivider: Boolean = false,
    visibleBottomGradient: Boolean = true,
    bottomGradientHeight: Dp = BOTTOM_GRADIENT_HEIGHT.dp,
    navigationBarContent: @Composable ColumnScope.() -> Unit,
    bottomFloatingContent: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.(gradientHeight: Dp) -> Unit
) {

    val providedBackground = LocalDToolBarColor.current.background
    val providedBorder = LocalDToolBarColor.current.border

    val backgroundColor =
        if (providedBackground != Color.Unspecified) providedBackground
        else CoreTheme.colors.background
    val borderColor =
        if (providedBorder != Color.Unspecified) providedBorder
        else CoreTheme.colors.border

    DScaffold(background = backgroundColor) {
        navigationBarContent()

        if (visibleDivider) {
            HorizontalDivider(color = borderColor)
        }

        Box(modifier = Modifier.weight(1f)) {
            val gradientHeight = when {
                visibleBottomGradient.not() -> 0.dp
                bottomFloatingContent != null -> BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp
                else -> bottomGradientHeight
            }
            content(gradientHeight)
            val rootBoxScope = this
            bottomFloatingContent?.let { bottomFloatingContent ->
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    bottomFloatingContent(rootBoxScope)
                }
            }
        }
    }
}

@Composable
fun DScaffold(
    background: Color = CoreTheme.colors.background,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.ime)
                .fillMaxSize()
                .background(background),
        ) {
            content()
        }
    }
}

@Composable
fun DNavigationBar(
    height: Dp = TITLE_BAR_HEIGHT.dp,
    horizontalPadding: Dp,
    content: @Composable RowScope.() -> Unit
) {

    DNavigationBar(
        height = height,
        padding = PaddingValues(horizontal = horizontalPadding),
        content = content,
    )
}

@Composable
fun DNavigationBar(
    height: Dp = TITLE_BAR_HEIGHT.dp,
    padding: PaddingValues = PaddingValues(horizontal = 4.dp),
    content: @Composable RowScope.() -> Unit
) {

    val backgroundColor = LocalDCommonColor.current.background

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(height)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

const val TITLE_BAR_HEIGHT = 64