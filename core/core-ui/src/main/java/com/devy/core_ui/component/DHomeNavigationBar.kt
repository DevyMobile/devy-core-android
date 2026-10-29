package com.devy.core_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.DHomeTabUiModel
import com.devy.core_ui.model.HOME_TAB_ID_ALL
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

@Composable
fun DHomeNavigationBar(
    tabs: List<DHomeTabUiModel>,
    selectedTabId: Int = HOME_TAB_ID_ALL,
    height: Dp = TITLE_BAR_HEIGHT.dp,
    visibleStartGradient: Boolean = true,
    horizontalPadding: Dp = 4.dp,
    style: TextStyle = CoreTheme.typo.title,
    onSelectedTab: (DHomeTabUiModel) -> Unit,
    trailingContent: @Composable RowScope.() -> Unit,
) {
    DHomeNavigationBar(
        tabs = tabs,
        selectedTabId = selectedTabId,
        height = height,
        padding = PaddingValues(horizontal = horizontalPadding),
        style = style,
        visibleStartGradient = visibleStartGradient,
        onSelectedTab = onSelectedTab,
        trailingContent = trailingContent
    )
}

@Composable
fun DHomeNavigationBar(
    tabs: List<DHomeTabUiModel>,
    selectedTabId: Int = HOME_TAB_ID_ALL,
    height: Dp = TITLE_BAR_HEIGHT.dp,
    padding: PaddingValues,
    startPadding: Dp = 12.dp,
    style: TextStyle = CoreTheme.typo.title,
    visibleStartGradient: Boolean = true,
    onSelectedTab: (DHomeTabUiModel) -> Unit,
    trailingContent: @Composable RowScope.() -> Unit,
) {

    val listState = rememberLazyListState()

    val showStartGradient by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 } }

    LaunchedEffect(selectedTabId) {
        listState.animateScrollToItem(
            when (selectedTabId) {
                HOME_TAB_ID_ALL -> 0
                else -> {
                    val tab = tabs.find { it.id == selectedTabId }
                    if (tab != null) {
                        tabs.indexOf(tab)
                    } else {
                        0
                    }
                }
            }
        )
    }

    DNavigationBar(
        height = height,
        padding = padding,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = startPadding, end = (48).dp),
                verticalAlignment = Alignment.CenterVertically,
                state = listState
            ) {
                item {
                    DHomeNavigationItem(
                        title = "전체",
                        selected = selectedTabId == HOME_TAB_ID_ALL,
                        style = style,
                        onClick = {
                            onSelectedTab(DHomeTabUiModel.All)
                        }
                    )
                }
                items(tabs) { tab ->
                    DHomeNavigationItem(
                        title = tab.title,
                        selected = selectedTabId == tab.id,
                        style = style,
                        onClick = {
                            onSelectedTab(tab)
                        }
                    )
                }
            }

            if (showStartGradient && visibleStartGradient) {
                DHorizontalGradient(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight(),
                    reverse = true,
                )
            }

            DHorizontalGradient(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
            )
        }

        trailingContent()
    }
}

@Composable
private fun DHomeNavigationItem(
    title: String,
    selected: Boolean,
    selectedColor: Color = CoreTheme.colors.onSurface0,
    unselectedColor: Color = CoreTheme.colors.onSurface60,
    style: TextStyle = CoreTheme.typo.title,
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier.noRippleClickable { onClick() },
        text = title,
        style = if (selected) style.copy(fontWeight = FontWeight.Bold) else style,
        color = if (selected) selectedColor else unselectedColor,
    )
}