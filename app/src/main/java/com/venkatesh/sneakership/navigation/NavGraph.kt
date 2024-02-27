package com.venkatesh.sneakership.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.venkatesh.sneakership.ui.screen.cart.CartScreen
import com.venkatesh.sneakership.ui.screen.dashboard.DashboardScreen
import com.venkatesh.sneakership.ui.screen.sneakerdetails.DetailScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier
){
    NavHost(navController = navController, startDestination = Screen.Home.route,modifier = modifier){
        composable(Screen.Home.route) {
            DashboardScreen(
                navigateToDetail = { sneakersId ->
                    navController.navigate(Screen.DetailSneakers.createRoute(sneakersId))
                }
            )
        }
        composable(
            route = Screen.DetailSneakers.route,
            arguments = listOf(navArgument("sneakersId") { type = NavType.StringType }),
        ) {
            val id = it.arguments?.getString("sneakersId") ?: ""
            DetailScreen(
                sneakersId = id,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToCart = {
                    navController.popBackStack()
                    navController.navigate(Screen.Cart.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(Screen.Cart.route) {
            val context = LocalContext.current
            CartScreen(
                onOrderButtonClicked = { message ->

                }
            )
        }

    }
}