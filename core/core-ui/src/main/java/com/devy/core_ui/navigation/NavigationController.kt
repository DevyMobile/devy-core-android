package com.devy.core_ui.navigation

import androidx.navigation.NavBackStackEntry

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

object NavigationController {
    private val destinations: MutableSet<DNavigationDestination> = mutableSetOf()

    fun addDestination(destination: DNavigationDestination) {
        destinations.add(destination)
    }

    fun getDestination(entry: NavBackStackEntry): DNavigationDestination? = destinations.find {it.route == entry.destination.route}
}

fun NavBackStackEntry.isModal(): Boolean = NavigationController.getDestination(this) is ModalDestination