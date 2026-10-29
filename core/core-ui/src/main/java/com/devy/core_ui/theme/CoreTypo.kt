package com.devy.core_ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Immutable
data class CoreTypography(
    val title1B: TextStyle,
    val title: TextStyle,
    val titleB: TextStyle,
    val body: TextStyle,
    val bodyM: TextStyle,
    val bodyB: TextStyle,
    val subhead: TextStyle,
    val subheadM: TextStyle,
    val subheadB: TextStyle,
    val footnote: TextStyle,
    val footnoteM: TextStyle,
    val footnoteB: TextStyle,
    val titleMini: TextStyle,
    val titleMiniB: TextStyle,
    val info: TextStyle,
    val infoM: TextStyle,
    val infoB: TextStyle,
    val caption: TextStyle,
    val captionM: TextStyle,
    val captionB: TextStyle,
) {

    companion object {
        fun createCoreTypography() = CoreTypography(
            title1B = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            title = TextStyle(fontWeight = FontWeight.Normal, fontSize = 22.sp),
            titleB = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
            body = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
            bodyM = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
            bodyB = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
            subhead = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp),
            subheadM = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
            subheadB = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            footnote = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
            footnoteM = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
            footnoteB = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            titleMini = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
            titleMiniB = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
            info = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
            infoM = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
            infoB = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
            caption = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp),
            captionM = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp),
            captionB = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp),
        )
    }
}