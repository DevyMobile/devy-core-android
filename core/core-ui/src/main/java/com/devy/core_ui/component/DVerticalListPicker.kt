package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.UnitCallback
import com.devy.core_ui.utils.noRippleClickable
import com.devy.core_ui.utils.toContentString
import kotlinx.coroutines.launch

@Composable
fun <T> DVerticalListPicker(
    items: List<T>,
    modifier: Modifier = Modifier,
    visibleCount: Int = 3,
    initialIndex: Int = 0,
    onSelectChanged: (T) -> Unit,
    displayTextMapper: (T) -> Any,
    onClickItem: UnitCallback = {},
) {
    val itemHeight = 48.dp
    val spacerCount = visibleCount / 2

    val providedPrimary = LocalDCommonColor.current.primary
    val primaryColor =
        if (providedPrimary != Color.Unspecified) providedPrimary else CoreTheme.colors.primary

    // Create a list with dummy spacers at the beginning and end
    val displayItems = remember(items) {
        val list = mutableListOf<T?>()
        repeat(spacerCount) { list.add(null) }
        list.addAll(items)
        repeat(spacerCount) { list.add(null) }
        list
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val coroutineScope = rememberCoroutineScope()

    var selectedIndex by remember { mutableStateOf(initialIndex) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (index in items.indices) {
                    selectedIndex = index
                    onSelectChanged(items[index])
                }
            }
    }

    Box(modifier = modifier.height(itemHeight * visibleCount)) {
        // Stationary selection background
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .height(itemHeight)
                .fillMaxWidth()
                .background(CoreTheme.colors.surface.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        )

        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior,
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(displayItems) { index, item ->
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .then(
                            if (item != null) {
                                Modifier.noRippleClickable {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index - spacerCount)
                                    }
                                    onClickItem()
                                }
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (item != null) {
                        val realIndex = index - spacerCount
                        Text(
                            text = displayTextMapper(item).toContentString(),
                            style = if (realIndex == selectedIndex) CoreTheme.typo.bodyB else CoreTheme.typo.body,
                            color = if (realIndex == selectedIndex) primaryColor else CoreTheme.colors.onSurface60,
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
    Column() {
        DVerticalListPicker(
            items = listOf("1", "2", "3", "4", "5"),
            onSelectChanged = {},
            onClickItem = {},
            displayTextMapper = { it }
        )
    }
}
