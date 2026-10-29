package com.devy.core_ui.navigation

import com.devy.core_ui.navigation.transition.ModalNavigationTransition
import com.devy.core_ui.navigation.transition.NavigationTransition

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

interface ModalDestination: DNavigationDestination {

    override fun provideTransition(): NavigationTransition = ModalNavigationTransition
}