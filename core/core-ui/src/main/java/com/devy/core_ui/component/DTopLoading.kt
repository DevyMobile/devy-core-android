package com.devy.core_ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.DTopLoadingAttr
import com.devy.core_ui.model.DTopLoadingAttrData
import com.devy.core_ui.theme.CoreTheme

@Composable
fun DTopLoading(
    visible: Boolean,
    attrData: DTopLoadingAttrData = DTopLoadingAttr(),
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(attrData.padding)
                .background(
                    color = attrData.background,
                    shape = RoundedCornerShape(20.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp),
                color = CoreTheme.colors.primary,
                strokeWidth = 3.dp,
            )

            attrData.text?.let { text ->
                Text(
                    text = text,
                    style = attrData.textStyle ?: CoreTheme.typo.body,
                    color = attrData.textColor ?: CoreTheme.colors.onSurface0,
                )
            }
        }
    }
}