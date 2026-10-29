package com.devy.core_ui.component.image

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.component.Image
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.conditional
import com.devy.core_ui.utils.noRippleClickable
import com.devy.core_ui.utils.toPx
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DImageViewer(
    imgSrcs: String,
    initIndex: Int = 0,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    onDismiss: () -> Unit,
) {
    DImageViewer(
        imgSrcs = listOf(imgSrcs),
        initIndex = initIndex,
        minScale = minScale,
        maxScale = maxScale,
        onDismiss = onDismiss
    )
}

private const val ANIMATION_DURATION = 300
private const val THUMBNAIL_WIDTH = 60
private const val THUMBNAIL_PADDING = 8

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DImageViewer(
    imgSrcs: List<String>,
    initIndex: Int = 0,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    onDismiss: () -> Unit,
) {

    val pagerState = rememberPagerState(
        initialPage = initIndex,
        pageCount = { imgSrcs.size }
    )
    val thumbnailState = rememberLazyListState(
        initialFirstVisibleItemIndex = initIndex
    )

    var selectedIndex by remember { mutableIntStateOf(initIndex) }

    var showToolBar by remember { mutableStateOf(true) }
    var boxWidthPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current
    val itemWidthPx = with(density) { THUMBNAIL_WIDTH.dp.toPx() }
    val spacingPx = with(density) { THUMBNAIL_PADDING.dp.toPx() }
    val itemCount = imgSrcs.size
    val totalContentWidthPx = (itemCount * itemWidthPx) + ((itemCount - 1) * spacingPx)
    val thumbnailScrollOffset by remember { derivedStateOf { ((boxWidthPx / 2) - (itemWidthPx / 2) - 16.dp.toPx()).toInt() * -1 } }

    LaunchedEffect(selectedIndex) {
        pagerState.animateScrollToPage(selectedIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        if (boxWidthPx < totalContentWidthPx) {
            thumbnailState.scrollToItem(
                index = pagerState.currentPage,
                scrollOffset = thumbnailScrollOffset
            )
        } else {
            thumbnailState.scrollToItem(pagerState.currentPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {
                showToolBar = !showToolBar
            }
            .onSizeChanged {
                boxWidthPx = it.width
            }
            .background(CoreTheme.colors.background) // 이미지 컨테이너 배경
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = true,
            pageSpacing = THUMBNAIL_PADDING.dp,
        ) { page ->
            ImageContent(
                imgSrc = imgSrcs[page],
                minScale = minScale,
                maxScale = maxScale,
            )
        }

        AnimatedVisibility(
            visible = showToolBar,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .noRippleClickable { onDismiss() },
                painter = painterResource(id = R.drawable.ic_close_circle_36),
                contentDescription = null,
                color = CoreTheme.colors.onSurface0
            )
        }

        AnimatedVisibility(
            visible = showToolBar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight }, // 화면 높이만큼 아래에서 시작
                animationSpec = tween(durationMillis = ANIMATION_DURATION)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight }, // 화면 높이만큼 아래로 이동하며 사라짐
                animationSpec = tween(durationMillis = ANIMATION_DURATION)
            )
        ) {
            LazyRow(
                state = thumbnailState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(imgSrcs) { index, imgSrc ->
                    DAsyncImage(
                        modifier = Modifier
                            .width(THUMBNAIL_WIDTH.dp)
                            .height(96.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                border = BorderStroke(
                                    width = if (pagerState.currentPage == index) 2.dp else 0.dp,
                                    color = if (pagerState.currentPage == index) CoreTheme.colors.border else CoreTheme.colors.onSurface90
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .noRippleClickable { selectedIndex = index },
                        model = imgSrc,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    BackHandler {
        onDismiss()
    }
}

@SuppressLint("ReturnFromAwaitPointerEventScope")
@Composable
fun ImageContent(
    modifier: Modifier = Modifier,
    imgSrc: String,
    minScale: Float,
    maxScale: Float,
    clipBounds: Boolean = false,
) {

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // 이미지를 표시하고 제스처를 처리하는 부분
    Box( // Image 대신 Box를 사용하여 배경색으로 예시
        modifier = modifier
            .fillMaxSize()
            .conditional(clipBounds) {
                clipToBounds()
            }
            // 이미지를 중앙에 배치하고, graphicsLayer로 스케일과 오프셋 적용
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x, // translationX/Y는 graphicsLayer의 오프셋
                translationY = offset.y
            )
            .pointerInput(Unit) { // 제스처 감지
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown() // 첫 번째 터치를 기다림

                        do {
                            val event = awaitPointerEvent()

                            // 스케일 계산
                            val zoom = event.calculateZoom()
                            scale = (scale * zoom).coerceIn(minScale, maxScale)

                            // 패닝(이동) 처리
                            if (scale > 1f) { // 이미지가 확대되었을 때만 이동 로직 실행
                                val pan = event.calculatePan()
                                val newOffset = offset + pan

                                // 화면 경계를 벗어나지 않도록 오프셋 제한
                                val maxX = (size.width * (scale - 1) / 2f)
                                val maxY = (size.height * (scale - 1) / 2f)

                                offset = Offset(
                                    x = newOffset.x.coerceIn(-maxX, maxX),
                                    y = newOffset.y.coerceIn(-maxY, maxY)
                                )
                            } else {
                                // 확대되지 않았다면 오프셋 초기화
                                offset = Offset.Zero
                            }

                            // 멀티터치(줌)가 아닌 단일터치 드래그일 경우,
                            // 확대되지 않은 상태에서는 이벤트를 소비하지 않아 HorizontalPager가 이벤트를 받을 수 있게 함
                            if (event.changes.size == 1 && scale == 1f) {
                                // 이벤트를 소비하지 않고 루프를 빠져나감으로써 페이저가 드래그를 처리하게 함
                            } else {
                                // 확대되었거나 멀티터치인 경우 모든 변경사항을 소비함
                                event.changes.forEach { it.consume() }
                            }
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
            .background(CoreTheme.colors.background) // 이미지 역할을 하는 예시 박스
    ) {
        DAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imgSrc,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ZoomableImage(
    modifier: Modifier = Modifier,
    imgSrc: String,
    minScale: Float,
    maxScale: Float,
    clipBounds: Boolean = true,
    onTap: (() -> Unit)? = null,
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val animationDurationMillis = 10
    val viewConfig = LocalViewConfiguration.current
    val density = LocalDensity.current
    val swipeThresholdPx = with(density) { 50.dp.toPx() }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .conditional(clipBounds) {
                clipToBounds()
            }
    ) {
        val maxWidth = constraints.maxWidth.toFloat()
        val maxHeight = constraints.maxHeight.toFloat()

        DAsyncImage(
            model = imgSrc,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onTap?.invoke() },
                        onDoubleTap = { tapOffset ->
                            val targetScale = when {
                                scale.value < (maxScale / 2f) * 0.95f -> maxScale / 2f
                                scale.value < maxScale * 0.95f -> maxScale
                                else -> 1f
                            }

                            scope.launch {
                                if (targetScale == 1f) {
                                    launch { scale.animateTo(1f, tween(animationDurationMillis)) }
                                    launch { offset.animateTo(Offset.Zero, tween(animationDurationMillis)) }
                                } else {
                                    val centerX = maxWidth / 2
                                    val centerY = maxHeight / 2

                                    val destOffset = Offset(
                                        x = (centerX - tapOffset.x) * (targetScale - 1),
                                        y = (centerY - tapOffset.y) * (targetScale - 1)
                                    )

                                    val maxX = (maxWidth * (targetScale - 1)) / 2
                                    val maxY = (maxHeight * (targetScale - 1)) / 2

                                    val limitedOffset = Offset(
                                        x = destOffset.x.coerceIn(-maxX, maxX),
                                        y = destOffset.y.coerceIn(-maxY, maxY)
                                    )

                                    launch { scale.animateTo(targetScale, tween(animationDurationMillis)) }
                                    launch { offset.animateTo(limitedOffset, tween(animationDurationMillis)) }
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)
                        var totalPan = Offset.Zero
                        var hasPassedSlop = false

                        do {
                            val event = awaitPointerEvent()
                            val zoomChange = event.calculateZoom()
                            val panChange = event.calculatePan()
                            totalPan += panChange

                            if (!hasPassedSlop) {
                                if (zoomChange != 1f || totalPan.getDistance() > viewConfig.touchSlop) {
                                    hasPassedSlop = true
                                    scope.launch {
                                        scale.stop()
                                        offset.stop()
                                    }
                                }
                            }

                            if (hasPassedSlop) {
                                val newScale = (scale.value * zoomChange).coerceIn(minScale, maxScale)
                                val maxX = (maxWidth * (newScale - 1)) / 2
                                val maxY = (maxHeight * (newScale - 1)) / 2

                                val isAtLeftEdge = offset.value.x <= -maxX && panChange.x < 0
                                val isAtRightEdge = offset.value.x >= maxX && panChange.x > 0
                                val isZoomed = newScale > 1.05f

                                if (isZoomed && !isAtLeftEdge && !isAtRightEdge) {
                                    event.changes.forEach { it.consume() }

                                    scope.launch {
                                        scale.snapTo(newScale)
                                        offset.snapTo(
                                            Offset(
                                                x = (offset.value.x + panChange.x).coerceIn(-maxX, maxX),
                                                y = (offset.value.y + panChange.y).coerceIn(-maxY, maxY)
                                            )
                                        )
                                    }
                                } else {
                                    scope.launch {
                                        scale.snapTo(newScale)
                                        offset.snapTo(
                                            Offset(
                                                x = offset.value.x.coerceIn(-maxX, maxX),
                                                y = (offset.value.y + panChange.y).coerceIn(-maxY, maxY)
                                            )
                                        )
                                    }
                                }
                            }
                        } while (event.changes.any { it.pressed })

                        // 스와이프 판정 (1f 상태에서 수직 이동이 수평보다 크고 임계값을 넘었을 때)
                        if (scale.value <= 1.05f) {
                            val absX = kotlin.math.abs(totalPan.x)
                            val absY = kotlin.math.abs(totalPan.y)
                            if (absY > absX && absY > swipeThresholdPx) {
                                if (totalPan.y < 0) {
                                    onSwipeUp?.invoke()
                                } else {
                                    onSwipeDown?.invoke()
                                }
                            }
                        }

                        // 제스처가 종료될 때 scale이 1f라면 offset을 확실히 초기화하여 중앙 정렬 보장
                        if (scale.value <= 1f) {
                            scope.launch {
                                offset.animateTo(Offset.Zero, tween(animationDurationMillis))
                            }
                        }
                    }
                }
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    translationX = offset.value.x,
                    translationY = offset.value.y
                )
        )
    }
}