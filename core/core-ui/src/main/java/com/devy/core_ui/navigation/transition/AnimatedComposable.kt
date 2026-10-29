package com.devy.core_ui.navigation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devy.core_ui.navigation.DNavigationDestination
import com.devy.core_ui.navigation.DummyDestination
import com.devy.core_ui.navigation.NavigationController

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

val LocalNavigationDestination = staticCompositionLocalOf<DNavigationDestination> { DummyDestination }

fun NavGraphBuilder.animatedComposable(
    destination: DNavigationDestination,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    val transition = destination.provideTransition()

    composable(
        destination.route,
        arguments,
        deepLinks,
        enterTransition = { transition.enterTransition(this)},
        exitTransition = { transition.exitTransition(this)},
        popEnterTransition = { transition.popEnterTransition(this)},
        popExitTransition = { transition.popExitTransition(this)},
        content = { entry ->
            ComposableContents(
                destination
            ) {
                content(entry)
            }

        }
    )
    NavigationController.addDestination(destination)
}

@Composable
private fun ComposableContents(
    destination: DNavigationDestination,
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalNavigationDestination provides destination,
    ) {
        content()
    }
}