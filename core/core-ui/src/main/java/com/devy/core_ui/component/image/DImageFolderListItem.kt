package com.devy.core_ui.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.component.Image
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.model.image.ImageFolderListItemUiModel
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

@Composable
fun DImageFolderListItem(
    data: ImageFolderListItemUiModel,
    keyword: String = "",
    pinned: Boolean = false,
    onClick: () -> Unit,
) {

    val providedPrimaryColor = LocalDCommonColor.current.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .noRippleClickable { onClick() }
    ) {
        Box(
            modifier = Modifier.size(48.dp)
        ) {
            DAsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CoreTheme.colors.onSurface40),
                model = data.thumbnail,
                contentScale = ContentScale.Crop,
            )

            if (pinned) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp)
                        .background(
                            color = CoreTheme.colors.background.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(id = R.drawable.ic_pin_solid_24)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            val titleAnnotatedString = buildAnnotatedString {
                append(data.title)
                if (keyword.isNotBlank()) {
                    val startIndex = data.title.indexOf(keyword)
                    val endIndex = startIndex + keyword.length
                    addStyle(
                        style = SpanStyle(
                            color = providedPrimaryColor,
                        ),
                        start = startIndex,
                        end = endIndex,
                    )
                }
            }
            Text(
                text = titleAnnotatedString,
                style = CoreTheme.typo.subhead,
                color = CoreTheme.colors.onSurface0,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = data.count.toString(),
                style = CoreTheme.typo.info,
                color = CoreTheme.colors.onSurface90,
            )
        }
    }
}