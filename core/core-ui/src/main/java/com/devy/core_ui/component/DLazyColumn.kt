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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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

/**
 * Created by JeYHoon on 2025. 11. 24..
 */

private const val TOP_GRADIENT_HEIGHT = 48
private const val SCROLL_BOX_WIDTH = 12

@Composable
fun DLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    visibleTopGradient: Boolean = true,
    visibleScrollBar: Boolean = true,
    topGradientHeight: Dp? = null,
    showTopLoading: Boolean = false,
    topLoadingAttr: DTopLoadingAttrData = DTopLoadingAttr(),
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: LazyListScope.() -> Unit
) {
    val isScrolled by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0 || state.firstVisibleItemScrollOffset > 0
        }
    }

    val scope = rememberCoroutineScope()

    // 스크롤 중이거나 드래그 중일 때만 표시하기 위한 상태
    var isDragging by remember { mutableStateOf(false) }

    val scrollbarInfo by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val totalItemsCount = layoutInfo.totalItemsCount

            if (visibleItems.isEmpty() || totalItemsCount == 0) {
                null
            } else {
                val firstVisibleItem = visibleItems.first()

                val visiblePercentage = visibleItems.size.toFloat() / totalItemsCount
                val scrollOffset = firstVisibleItem.index.toFloat() / totalItemsCount

                Pair(scrollOffset, visiblePercentage)
            }
        }
    }

    val isVisible = state.isScrollInProgress || isDragging
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = if (isVisible) 100 else 500),
        label = "scrollbar_alpha"
    )

    Column {
        DTopLoading(
            visible = showTopLoading,
            attrData = topLoadingAttr
        )

        Box {
            LazyColumn(
                modifier = modifier,
                state = state,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
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

            // 스크롤바 영역
            if (visibleScrollBar) {
                scrollbarInfo?.let { (offsetRatio, heightRatio) ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(SCROLL_BOX_WIDTH.dp)
                            .padding(end = 2.dp)
                            .align(Alignment.CenterEnd)
                            .pointerInput(Unit) {
                                detectVerticalDragGestures(
                                    onDragStart = { isDragging = true },
                                    onDragEnd = { isDragging = false },
                                    onDragCancel = { isDragging = false },
                                    onVerticalDrag = { change, _ ->
                                        change.consume()
                                        val totalItems = state.layoutInfo.totalItemsCount
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
                        val alpha by animateFloatAsState(
                            targetValue = if (state.isScrollInProgress || isDragging) 1f else 0f,
                            animationSpec = tween(300)
                        )

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val barHeight = size.height * heightRatio
                            val barOffset = size.height * offsetRatio

                            drawRoundRect(
                                color = Color.Gray.copy(alpha = 0.5f * alpha),
                                topLeft = Offset(
                                    size.width - SCROLL_BOX_WIDTH.dp.toPx(),
                                    barOffset
                                ), // 오른쪽 끝에서 살짝 띄움
                                size = Size(6.dp.toPx(), barHeight),
                                cornerRadius = CornerRadius(10f, 10f)
                            )
                        }
                    }
                }
            }
        }
    }
}