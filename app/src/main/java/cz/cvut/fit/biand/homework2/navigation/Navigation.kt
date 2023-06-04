package cz.cvut.fit.biand.homework2.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.presentation.detail.DetailScreen
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters.ListScreen
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters.ListTopBar
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.favorites.FavoritesScreen
import cz.cvut.fit.biand.homework2.features.characters.presentation.search.SearchScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.BottomNavScreen.route,
    ) {

        composable(
            route = Screens.BottomNavScreen.route,
        ) {
            BottomNavScreen(
                navigateToCharacterDetail = { navController.navigate(Screens.DetailScreen(it).route) },
                navigateToSearch = { navController.navigate(Screens.SearchScreen.route) },
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

@Composable
fun BottomNavScreen(
    navigateToCharacterDetail: (String) -> Unit,
    navigateToSearch: () -> Unit,
) {

    val focusRequester = remember { FocusRequester() }
    val bottomNavController = rememberNavController()

    @StringRes val screenTitle = when (currentRoute(navController = bottomNavController)?.route) {
        BottomBarScreen.FavoritesScreen.route -> BottomBarScreen.FavoritesScreen.title
        else -> BottomBarScreen.ListScreen.title
    }

    Scaffold(
        topBar = {
            ListTopBar(
                title = stringResource(id = screenTitle),
                searchIconAction = navigateToSearch,
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
        bottomBar = {
            BottomBar(
                bottomNavController = bottomNavController
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomBarScreen.ListScreen.route
        ) {
            composable(route = BottomBarScreen.ListScreen.route) {
                ListScreen(
                    navigateToCharacterDetail = navigateToCharacterDetail,
                    paddingValues = paddingValues
                )
            }
            composable(route = BottomBarScreen.FavoritesScreen.route) {
                FavoritesScreen(
                    navigateToCharacterDetail = navigateToCharacterDetail,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    bottomNavController: NavController
) {
    val screens = listOf(
        BottomBarScreen.ListScreen,
        BottomBarScreen.FavoritesScreen,
    )

    BottomAppBar(
        modifier = Modifier
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 4.dp
    ) {
        Spacer(modifier = Modifier.weight(1f))
        screens.forEach { screen ->
            BottomBarItem(
                screen = screen,
                currentScreen = currentRoute(navController = bottomNavController),
                modifier = Modifier.weight(4f),
                onItemClick = {
                    bottomNavController.navigate(screen.route) {
                        popUpTo(bottomNavController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun RowScope.BottomBarItem(
    screen: BottomBarScreen,
    currentScreen: NavDestination?,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    BottomNavigationItem(
        modifier = modifier,
        label = {
            androidx.compose.material.Text(
                text = stringResource(id = screen.title),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = stringResource(R.string.navigation_icon)
            )
        },
        selected = currentScreen?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = MaterialTheme.colorScheme.outline,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        onClick = onItemClick
    )
}

@Composable
private fun currentRoute(navController: NavController): NavDestination? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination
}