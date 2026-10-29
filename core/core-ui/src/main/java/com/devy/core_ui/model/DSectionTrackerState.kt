package com.devy.core_ui.model

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

class DSectionTrackerState(
    initialRects: Map<Int, Rect> = emptyMap()
) {

    // 1. 위치 정보를 담는 Map (MutableStateMap)
    val itemRects = mutableStateMapOf<Int, Rect>().apply { putAll(initialRects) }

    // 2. 부모 컨테이너의 위치
    var parentPositionInWindow by mutableStateOf(Offset.Zero)

    /**
     * 특정 아이템의 Rect를 업데이트 (부모 좌표계 기준)
     */
    fun updateItemPosition(id: Int, coordinates: LayoutCoordinates) {
        val itemPositionInWindow = coordinates.positionInWindow()
        val itemSize = coordinates.size.toSize()
        val localOffset = itemPositionInWindow - parentPositionInWindow
        itemRects[id] = Rect(localOffset, itemSize)
    }

    /**
     * 부모 컨테이너의 위치를 업데이트
     */
    fun updateParentPosition(coordinates: LayoutCoordinates) {
        parentPositionInWindow = coordinates.positionInWindow()
    }

    companion object {
        // Saver 구현: Map<Int, Rect>를 List<List<Any>> 형태로 직렬화하여 저장
        val Saver: Saver<DSectionTrackerState, *> = Saver(
            save = { state ->
                state.itemRects.entries.map { (key, rect) ->
                    listOf(key, rect.left, rect.top, rect.right, rect.bottom)
                }
            },
            restore = { savedValue ->
                val outerList = savedValue as List<List<Any>>
                val restoredMap = outerList.associate { innerList ->
                    val key = innerList[0] as Int
                    val rect = Rect(
                        left = innerList[1] as Float,
                        top = innerList[2] as Float,
                        right = innerList[3] as Float,
                        bottom = innerList[4] as Float
                    )
                    key to rect
                }
                DSectionTrackerState(restoredMap)
            }
        )
    }
}

/**
 * Composable에서 SelectionTrackerState를 생성하고 기억함
 */
@Composable
fun rememberSelectionTrackerState(): DSectionTrackerState {
    return rememberSaveable(saver = DSectionTrackerState.Saver) {
        DSectionTrackerState()
    }
}

/**
 * 부모 컨테이너(Column 등)에 적용하여 위치를 추적하고 보더를 그림
 */
@Composable
fun Modifier.selectionBorderContainer(
    state: DSectionTrackerState,
    selectedId: Int?,
    color: Color,
    cornerRadius: Dp = 100.dp,
    style: DrawStyle = Fill,
): Modifier {
    // 선택된 아이템의 Rect를 찾고 애니메이션 처리
    val targetRect = if (selectedId != null) state.itemRects[selectedId] ?: Rect.Zero else Rect.Zero

    val animatedRect by animateRectAsState(
        targetValue = targetRect,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "SelectionBorder"
    )

    return this
        .onGloballyPositioned { state.updateParentPosition(it) }
        .drawBehind {
            if (animatedRect != Rect.Zero) {
                drawRoundRect(
                    color = color,
                    topLeft = animatedRect.topLeft,
                    size = animatedRect.size,
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                    style = style,
                )
            }
        }
}

/**
 * 각 개별 아이템에 적용하여 자신의 위치를 State에 알림
 */
fun Modifier.trackSelectedItem(
    id: Int,
    state: DSectionTrackerState
): Modifier {
    return this.onGloballyPositioned {
        state.updateItemPosition(id, it)
    }
}