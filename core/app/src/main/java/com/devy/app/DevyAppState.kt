package com.devy.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.devy.core_ui.navigation.DNavigationDestination
import kotlinx.coroutines.CoroutineScope

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Composable
fun rememberDevyAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    popBack: () -> Unit,
): DevyAppState {
    return remember(navController, coroutineScope, popBack) {
        DevyAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            popBack = popBack,
        )
    }
}

@Stable
class DevyAppState(
    coroutineScope: CoroutineScope,
    val navController: NavHostController,
    val popBack: () -> Unit,
) {
    fun navigateToDestination(destination: DNavigationDestination) {

    }
}