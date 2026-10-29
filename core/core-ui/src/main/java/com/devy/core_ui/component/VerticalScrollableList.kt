package com.devy.core_ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.abs

/**
 * Created by JeYHoon on 2025. 10. 9..
 */

/**
 * 중앙 아이템이 선택될 때 콜백을 호출하는 리스트
 *
 * @param items 표시할 데이터 리스트
 * @param visibleCount 화면에 보여줄 아이템 개수 (홀수 권장)
 * @param modifier 추가 Modifier
 * @param onItemSelected 아이템이 중앙에 위치할 때 호출되는 콜백. 선택된 아이템을 인자로 받음
 * @param itemContent 각 아이템을 그리는 Composable. (data, isCentered)를 인자로 받음
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> VerticalScrollableList(
    items: List<T>,
    visibleCount: Int,
    modifier: Modifier = Modifier,
    onItemSelected: (item: T) -> Unit,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit
) {
    if (items.isEmpty()) return

    val safeVisibleCount = visibleCount // min(visibleCount, items.size)

    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val density = LocalDensity.current

    val centeredItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) -1
            else {
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }?.index
                    ?: -1
            }
        }
    }

    // centeredItemIndex가 변경될 때마다 콜백을 호출
    // listState.isScrollInProgress가 false일 때만 호출하여 스크롤 중 무분별한 호출 방지
    LaunchedEffect(centeredItemIndex, listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && centeredItemIndex != -1) {
            onItemSelected(items[centeredItemIndex])
        }
    }

    SubcomposeLayout(modifier = modifier) { constraints ->
        // 아이템 높이 측정
        val itemPlaceable = subcompose("measureItem") {
            itemContent(items.first(), false)
        }.first().measure(constraints)

        val itemHeight = itemPlaceable.height.toFloat()
        val itemHeightDp = with(density) { itemHeight.toDp() }

        // 리스트 높이 및 패딩 계산
        val listHeight = itemHeight * safeVisibleCount
        val verticalPadding = (listHeight / 2 - itemHeight / 2).coerceAtLeast(0f)
        val verticalPaddingDp = with(density) { verticalPadding.toDp() }

        val gradientModifier = Modifier.fillMaxWidth().height(itemHeightDp)

        val listPlaceable = subcompose("list") {
            Box(modifier = Modifier.height(itemHeightDp)) {
                LazyColumn(
                    state = listState,
                    flingBehavior = flingBehavior,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = verticalPaddingDp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(items) { index, item ->
                        val isSelected = (index == centeredItemIndex)
                        itemContent(item, isSelected)
                    }
                }

                GradientOverlay(
                    modifier = gradientModifier,
                    type = GradientType.Top
                )

                GradientOverlay(
                    modifier = gradientModifier,
                    type = GradientType.Bottom
                )
            }
        }.first().measure(
            constraints.copy(
                minHeight = listHeight.toInt(),
                maxHeight = listHeight.toInt()
            )
        )

        layout(listPlaceable.width, listPlaceable.height) {
            listPlaceable.place(0, 0)
        }
    }
}
