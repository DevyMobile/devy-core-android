package com.devy.core_ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import com.devy.core_ui.theme.CoreTheme
import com.halilibo.richtext.commonmark.Markdown
import com.halilibo.richtext.ui.BasicRichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.RichTextThemeProvider
import com.halilibo.richtext.ui.string.RichTextStringStyle

/**
 * Created by JeYHoon on 2025. 11. 8..
 */

@Composable
fun DMarkdownText(
    modifier: Modifier = Modifier,
    content: String,
    style: TextStyle = CoreTheme.typo.body,
    color: Color = CoreTheme.colors.onSurface0,
    linkColor: Color = CoreTheme.colors.primary,
    headingStyle: TextStyle = CoreTheme.typo.title1B
) {

    RichTextThemeProvider(
        textStyleProvider = { style },
        contentColorProvider = { color }
    ) {
        BasicRichText(
            modifier = modifier,
            style = RichTextStyle(
                headingStyle = { _, _ ->
                    headingStyle
                },
                stringStyle = RichTextStringStyle(
                    linkStyle = TextLinkStyles(
                        style = style.copy(color = linkColor).toSpanStyle()
                    )
                )
            )
        ) {
            Markdown(content)
        }
    }
}