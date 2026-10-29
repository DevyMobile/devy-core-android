package com.devy.core_ui.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.devy.core_ui.R
import com.devy.core_ui.component.Image
import com.devy.core_ui.model.image.ImagePickerUiModel
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

@Composable
fun LazyGridItemScope.DImagePickerItem(
    data: ImagePickerUiModel,
    color: Color? = null,
    onClickFull: (imgSrc: String) -> Unit,
    onChecked: (imgSrc: String) -> Unit,
) {

    val primaryColor = color ?: CoreTheme.colors.primary

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .noRippleClickable { onChecked(data.imgSrc) },
    ) {
        DAsyncImage(
            model = data.imgSrc.toUri(),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp),
            painter = painterResource(id = if (data.checked) R.drawable.ic_radio_checked_24 else R.drawable.ic_radio_unchecked_24),
            contentDescription = null,
            color = if (data.checked) primaryColor else CoreTheme.colors.onSurface90,
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.BottomStart)
                .noRippleClickable { onClickFull(data.imgSrc) },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_full_24),
                color = CoreTheme.colors.onSurface0
            )
        }

        if (data.isGif) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 4.dp)
                    .background(
                        color = CoreTheme.colors.onSurface40.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = "GIF",
                style = CoreTheme.typo.info,
                color = CoreTheme.colors.onSurface0,
            )
        }
    }
}