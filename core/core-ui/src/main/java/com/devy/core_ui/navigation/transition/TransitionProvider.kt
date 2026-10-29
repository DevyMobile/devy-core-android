package com.devy.core_ui.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

interface TransitionProvider {
    fun provideTransition(): NavigationTransition
}

interface NavigationTransition {
    fun enterTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition
    fun exitTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition
    fun popEnterTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition
    fun popExitTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition
}