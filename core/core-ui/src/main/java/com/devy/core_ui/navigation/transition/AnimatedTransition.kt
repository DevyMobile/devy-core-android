package com.devy.core_ui.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import com.devy.core_ui.navigation.isModal
import com.devy.core_ui.navigation.transition.AnimatedTransition.slideDownExit
import com.devy.core_ui.navigation.transition.AnimatedTransition.slideLeftEnter
import com.devy.core_ui.navigation.transition.AnimatedTransition.slideLeftExit
import com.devy.core_ui.navigation.transition.AnimatedTransition.slideRightEnter
import com.devy.core_ui.navigation.transition.AnimatedTransition.slideRightExit
import com.devy.core_ui.navigation.transition.AnimatedTransition.slideUpEnter

/**
 * Created by JeYHoon on 2025. 10. 2..
 */
object ScreenNavigationTransition : NavigationTransition {
    override fun enterTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition =
        transitionScope.slideLeftEnter()

    override fun exitTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition =
        if (transitionScope.targetState.isModal()) ExitTransition.None else transitionScope.slideLeftExit()

    override fun popEnterTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition =
        if (transitionScope.initialState.isModal()) EnterTransition.None else transitionScope.slideRightEnter()

    override fun popExitTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition =
        transitionScope.slideRightExit()
}

object ModalNavigationTransition : NavigationTransition {
    override fun enterTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition =
        transitionScope.slideUpEnter()

    override fun exitTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition =
        if (transitionScope.targetState.isModal()) ExitTransition.None else transitionScope.slideLeftExit()

    override fun popEnterTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition =
        if (transitionScope.initialState.isModal()) EnterTransition.None else transitionScope.slideRightEnter()

    override fun popExitTransition(transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition =
        transitionScope.slideDownExit()
}

object AnimatedTransition {
    private val animationSpec: FiniteAnimationSpec<IntOffset> = tween(300)

    fun AnimatedContentTransitionScope<NavBackStackEntry>.slideLeftEnter(): EnterTransition =
        slideIntoContainer(SlideDirection.Left, animationSpec)

    fun AnimatedContentTransitionScope<NavBackStackEntry>.slideLeftExit(): ExitTransition =
        slideOutOfContainer(SlideDirection.Left, animationSpec)

    fun AnimatedContentTransitionScope<NavBackStackEntry>.slideRightEnter(): EnterTransition =
        slideIntoContainer(SlideDirection.Right, animationSpec)

    fun AnimatedContentTransitionScope<NavBackStackEntry>.slideRightExit(): ExitTransition =
        slideOutOfContainer(SlideDirection.Right, animationSpec)

    fun AnimatedContentTransitionScope<NavBackStackEntry>.slideUpEnter(): EnterTransition =
        slideIntoContainer(SlideDirection.Up, animationSpec)

    fun AnimatedContentTransitionScope<NavBackStackEntry>.slideDownExit(): ExitTransition =
        slideOutOfContainer(SlideDirection.Down, animationSpec)
}