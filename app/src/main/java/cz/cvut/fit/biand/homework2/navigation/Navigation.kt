package cz.cvut.fit.biand.homework2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.fit.biand.homework2.features.characters.presentation.detail.DetailScreen
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.ListScreen
import cz.cvut.fit.biand.homework2.features.characters.presentation.search.SearchScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Characters.route,
    ) {
        composable(route = BottomBarScreen.Characters.route) {
            ListScreen(
                navigateToSearch = { navController.navigate(Screens.SearchScreen.route) },
                navigateToCharacterDetail = {
                    navController.navigate(Screens.DetailScreen(it).route)
                },
                onBottomNavItemClick = { bottomBarScreenRoute ->
                    navController.navigate(bottomBarScreenRoute) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                } },
                currentScreen = BottomBarScreen.Characters
            )
        }
        // TODO: remove duplicate and inject the same instance of viewModel:
        composable(route = BottomBarScreen.Favorites.route) {
            ListScreen(
                navigateToSearch = { navController.navigate(Screens.SearchScreen.route) },
                navigateToCharacterDetail = {
                    navController.navigate(Screens.DetailScreen(it).route)
                },
                onBottomNavItemClick = { bottomBarScreenRoute ->
                    navController.navigate(bottomBarScreenRoute) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    } },
                currentScreen = BottomBarScreen.Favorites
            )
        }

        composable(
            route = Screens.DetailScreen("{${Screens.DetailScreen.ID}}").route,
            arguments = listOf(
                navArgument(name = Screens.DetailScreen.ID) {
                    type = NavType.StringType
                },
            ),
        ) {
            DetailScreen(
                navigateUp = { navController.navigateUp() },
            )
        }

        composable(route = Screens.SearchScreen.route) {
            SearchScreen(
                navigateToCharacterDetail = {
                    navController.navigate(Screens.DetailScreen(it).route)
                },
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}
