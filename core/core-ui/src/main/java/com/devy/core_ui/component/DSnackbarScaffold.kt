package com.devy.core_ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.devy.core_ui.component.local.LocalShowSnackbar
import com.devy.core_ui.component.snackbar.DSnackbar
import com.devy.core_ui.viewmodel.DSnackbarViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DSnackbarScaffold(
    modifier: Modifier = Modifier,
    snackbarViewModel: DSnackbarViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalShowSnackbar provides snackbarViewModel::showSnackbar
    ) {
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    DSnackbar(data)
                }
            }
        ) { contentPadding ->
            content(contentPadding)
        }
    }

    LaunchedEffect(snackbarViewModel.snackbar) {
        snackbarViewModel.snackbar.collectLatest { snackbar ->
            snackbarHostState.showSnackbar(
                message = snackbar.message,
                duration = snackbar.duration
            )
        }
    }
}