package com.devy.core_ui.navigation

import com.devy.core_ui.navigation.transition.NavigationTransition
import com.devy.core_ui.navigation.transition.ScreenNavigationTransition
import com.devy.core_ui.navigation.transition.TransitionProvider

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

interface DNavigationDestination: TransitionProvider {
    val route: String
    override fun provideTransition(): NavigationTransition = ScreenNavigationTransition
}