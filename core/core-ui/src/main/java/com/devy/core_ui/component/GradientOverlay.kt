package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme

/**
 * Created by JeYHoon on 2025. 10. 9..
 */
enum class GradientType {
    Top, Bottom, Start, End
}

/**
 * 지정된 타입에 따라 그라데이션 오버레이를 표시하는 Composable
 * @param type 그라데이션의 방향과 색상 순서를 결정
 */
@Composable
fun BoxScope.GradientOverlay(
    modifier: Modifier = Modifier,
    type: GradientType
) {

    val background = LocalDCommonColor.current.background

    // when 표현식을 사용해 type에 따라 브러시와 정렬을 한 번에 결정합니다.
    val (brush, alignment) = when (type) {
        GradientType.Top -> Brush.verticalGradient(
            colors = listOf(background, CoreTheme.colors.tr)
        ) to Alignment.TopCenter

        GradientType.Bottom -> Brush.verticalGradient(
            colors = listOf(CoreTheme.colors.tr, background)
        ) to Alignment.BottomCenter

        GradientType.Start -> Brush.horizontalGradient(
            colors = listOf(background, CoreTheme.colors.tr)
        ) to Alignment.CenterStart

        GradientType.End -> Brush.horizontalGradient(
            colors = listOf(CoreTheme.colors.tr, background)
        ) to Alignment.CenterEnd
    }

    Box(
        modifier = modifier
            .align(alignment) // 결정된 정렬 적용
            .background(brush) // 결정된 브러시 적용
    )
}

@Composable
fun GradientOverlay(
    modifier: Modifier = Modifier,
    gradientColor: Color? = null,
) {

    val providedBackground = LocalDCommonColor.current.background
    val background = gradientColor ?: providedBackground

    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(CoreTheme.colors.tr, background)
                )
            )
    )
}