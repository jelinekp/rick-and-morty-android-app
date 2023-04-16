package cz.cvut.fit.biand.homework2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.fit.biand.homework2.presentation.DetailViewModel
import cz.cvut.fit.biand.homework2.presentation.ListViewModel
import cz.cvut.fit.biand.homework2.presentation.SearchViewModel
import cz.cvut.fit.biand.homework2.system.DetailScreen
import cz.cvut.fit.biand.homework2.system.ListScreen
import cz.cvut.fit.biand.homework2.system.SearchScreen

@Composable
fun Navigation(
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    searchViewModel: SearchViewModel,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ListScreen.route,
    ) {
        composable(route = Screen.ListScreen.route) {
            ListScreen(
                navController = navController,
                viewModel = listViewModel,
            )
        }
        composable(
            route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                },
            ),
        ) { entry ->
            DetailScreen(
                navController = navController,
                viewModel = detailViewModel,
                id = entry.arguments?.getInt("id"),
            )
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(
                navController = navController,
                viewModel = searchViewModel,
            )
        }
    }
}
