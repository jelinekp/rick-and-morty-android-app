package cz.cvut.fit.biand.homework2.features.characters.presentation.list

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.BottomAppBar
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadingState
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters.LoadedState
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.favorites.FavoriteCharactersList
import cz.cvut.fit.biand.homework2.navigation.BottomBarScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Screen representing a list of characters for "Characters" and "Favorites"
 */
@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateToSearch: () -> Unit
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    ListScreen(
        screenState = screenState,
        navigateToCharacterDetail = navigateToCharacterDetail,
        navigateToSearch = navigateToSearch
    )
}

@Composable
private fun ListScreen(
    screenState: ListScreenState,
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateToSearch: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val bottomNavController = rememberNavController()

    @StringRes val screenTitle = when (currentRoute(navController = bottomNavController)?.route) {
        BottomBarScreen.Favorites.route -> BottomBarScreen.Favorites.title
        else -> BottomBarScreen.Characters.title
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (screenState) {
                is ListScreenState.Loading -> LoadingState()
                is ListScreenState.Loaded -> ListScreenNavHost(
                    bottomNavController = bottomNavController,
                    screenState = screenState,
                    navigateToCharacterDetail = navigateToCharacterDetail
                )
            }
        }
    }
}

@Composable
private fun ListScreenNavHost(
    bottomNavController: NavHostController,
    screenState: ListScreenState.Loaded,
    navigateToCharacterDetail: (id: String) -> Unit,
) {
    NavHost(
        navController = bottomNavController,
        startDestination = BottomBarScreen.Characters.route
    ) {
        composable(BottomBarScreen.Characters.route) {
            LoadedState(
                charactersResult = screenState.charactersResult,
                errorText = stringResource(R.string.outdated_data_message),
                onCharacterClick = { navigateToCharacterDetail(it.id) }
            )
        }
        composable(BottomBarScreen.Favorites.route) {
            FavoriteCharactersList(
                favoriteCharacters = screenState.favoriteCharacters.collectAsStateWithLifecycle(
                    initialValue = emptyList()
                ).value,
                onCharacterClicked = { navigateToCharacterDetail(it.id) },
            )
        }
    }
}

@Composable
private fun ListTopBar(
    title: String?,
    searchIconAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colorScheme.background,
        title = {
            Text(
                text = title ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = null,
        actions = {
            IconButton(onClick = searchIconAction) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.searchIcon),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        modifier = modifier//.height(48.dp)
    )
}

@Composable
private fun BottomBar(
    bottomNavController: NavController
) {
    val screens = listOf(
        BottomBarScreen.Characters,
        BottomBarScreen.Favorites
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