package com.devy.core_ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.DTopLoadingAttr
import com.devy.core_ui.model.DTopLoadingAttrData
import kotlinx.coroutines.launch

private const val TOP_GRADIENT_HEIGHT = 48
private const val SCROLL_BOX_WIDTH = 12

@Composable
fun DLazyVerticalGrid(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    columns: GridCells,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    visibleTopGradient: Boolean = true,
    visibleScrollBar: Boolean = true,
    topGradientHeight: Dp? = null,
    showTopLoading: Boolean = false,
    topLoadingAttr: DTopLoadingAttrData = DTopLoadingAttr(),
    content: LazyGridScope.() -> Unit,
) {

    val isScrolled by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0 || state.firstVisibleItemScrollOffset > 0
        }
    }

    val scope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }

    // 1. derivedStateOf를 통한 그리드 상태 계산 최적화
    val scrollbarInfo by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val visibleItems = layoutInfo.visibleItemsInfo

            if (visibleItems.isEmpty() || totalItems == 0) null
            else {
                // 그리드에서도 전체 아이템 대비 현재 첫 아이템의 인덱스로 비율을 계산합니다.
                val firstVisibleItem = visibleItems.first()
                val heightRatio = (visibleItems.size.toFloat() / totalItems).coerceIn(0.1f, 1f)
                val offsetRatio = (firstVisibleItem.index.toFloat() / totalItems)
                Pair(offsetRatio, heightRatio)
            }
        }
    }

    val isVisible = state.isScrollInProgress || isDragging
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(if (isVisible) 100 else 500),
        label = "grid_scrollbar_alpha"
    )

    Column {
        DTopLoading(
            visible = showTopLoading,
            attrData = topLoadingAttr
        )

        Box {
            LazyVerticalGrid(
                modifier = modifier,
                state = state,
                columns = columns,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                verticalArrangement = verticalArrangement,
                horizontalArrangement = horizontalArrangement,
                flingBehavior = flingBehavior,
                userScrollEnabled = userScrollEnabled,
                overscrollEffect = overscrollEffect,
                content = content
            )
            if (visibleTopGradient && isScrolled) {
                GradientOverlay(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topGradientHeight ?: TOP_GRADIENT_HEIGHT.dp),
                    type = GradientType.Top
                )
            }

            if (visibleScrollBar) {
                scrollbarInfo?.let { (offsetRatio, heightRatio) ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(SCROLL_BOX_WIDTH.dp) // 터치 영역 확장
                            .align(Alignment.CenterEnd)
                            .pointerInput(Unit) {
                                detectVerticalDragGestures(
                                    onDragStart = { isDragging = true },
                                    onDragEnd = { isDragging = false },
                                    onDragCancel = { isDragging = false },
                                    onVerticalDrag = { change, _ ->
                                        change.consume()
                                        val totalItems = state.layoutInfo.totalItemsCount
                                        // 터치 좌표 비율을 인덱스로 변환
                                        val targetIndex =
                                            ((change.position.y / size.height) * totalItems).toInt()
                                        scope.launch {
                                            state.scrollToItem(
                                                targetIndex.coerceIn(
                                                    0,
                                                    totalItems - 1
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val barHeight = size.height * heightRatio
                            val barOffset = size.height * offsetRatio

                            drawRoundRect(
                                color = Color.Gray.copy(alpha = 0.5f * alpha),
                                topLeft = Offset(
                                    size.width - SCROLL_BOX_WIDTH.dp.toPx(),
                                    barOffset
                                ),
                                size = Size(6.dp.toPx(), barHeight),
                                cornerRadius = CornerRadius(10f, 10f) // 둥근 모서리
                            )
                        }
                    }
                }
            }
        }
    }
}