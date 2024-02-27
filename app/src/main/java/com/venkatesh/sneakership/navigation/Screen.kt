package com.venkatesh.sneakership.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object About : Screen("wish")
    object DetailSneakers : Screen("home/{sneakersId}") {
        fun createRoute(sneakersId: String) = "home/$sneakersId"
    }
}