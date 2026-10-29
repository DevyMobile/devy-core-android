package com.devy.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.FloatingActionButtonInfo
import com.devy.core_ui.utils.toDp

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Composable
fun DFloatingActionButtonLayout(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(end = 16.dp, bottom = 24.dp),
    alignment: Alignment = Alignment.BottomEnd,
    floatingContent: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.(floatingContentHeight: Dp) -> Unit,
) {
    var floatingContentHeight by remember { mutableStateOf(paddingValues.calculateBottomPadding()) }

    Box(modifier = modifier) {
        content(floatingContentHeight)

        Box(
            modifier = Modifier
                .align(alignment)
                .padding(paddingValues)
                .onSizeChanged {
                    floatingContentHeight =
                        it.height.toDp() + paddingValues.calculateBottomPadding()
                }
        ) {
            floatingContent()
        }
    }
}

@Composable
fun DFloatingActionButtonBox(
    modifier: Modifier = Modifier,
    floatingActionButtonInfo: FloatingActionButtonInfo?,
    paddingValues: PaddingValues = PaddingValues(end = 16.dp, bottom = 24.dp),
    alignment: Alignment = Alignment.BottomEnd,
    content: @Composable BoxScope.(floatingContentHeight: Dp) -> Unit,
) {

    DFloatingActionButtonLayout(
        modifier = modifier,
        paddingValues = paddingValues,
        alignment = alignment,
        floatingContent = {
            floatingActionButtonInfo?.let {
                FloatingActionButton(it)
            }
        },
        content = content,
    )
}

@Composable
fun DFloatingActionButtonRow(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(end = 16.dp, bottom = 24.dp),
    alignment: Alignment = Alignment.BottomEnd,
    floatingContent: @Composable RowScope.() -> Unit,
    content: @Composable BoxScope.(floatingContentHeight: Dp) -> Unit,
) {

    DFloatingActionButtonLayout(
        modifier = modifier,
        paddingValues = paddingValues,
        alignment = alignment,
        floatingContent = {
            Row { floatingContent() }
        },
        content = content,
    )
}

@Composable
fun DFloatingActionButtonColumn(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(end = 16.dp, bottom = 24.dp),
    alignment: Alignment = Alignment.BottomEnd,
    floatingContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.(floatingContentHeight: Dp) -> Unit,
) {

    DFloatingActionButtonLayout(
        modifier = modifier,
        paddingValues = paddingValues,
        alignment = alignment,
        floatingContent = {
            Column { floatingContent() }
        },
        content = content,
    )
}