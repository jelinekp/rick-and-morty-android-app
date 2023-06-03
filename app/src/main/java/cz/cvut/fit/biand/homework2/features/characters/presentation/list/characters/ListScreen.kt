package cz.cvut.fit.biand.homework2.features.characters.presentation.list

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import coil.compose.AsyncImage
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.FavoriteIconIndicator
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadingState
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

    /*@StringRes val screenTitle = when (currentRoute(navController = bottomNavController)?.route) {
        BottomBarScreen.Favorites.route -> BottomBarScreen.FavoritesScreen.title
        else -> BottomBarScreen.ListScreen.title
    }*/

    Scaffold(
        topBar = {
            ListTopBar(
                title = stringResource(id = BottomBarScreen.ListScreen.title),
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
                is ListScreenState.Loaded -> LoadedState(
                    charactersResult = screenState.charactersResult,
                    isSuccess = screenState.isSuccess,
                    errorText = stringResource(R.string.outdated_data_message)
                ) { navigateToCharacterDetail(it) }

                else -> {}
            }
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
        modifier = modifier
    )
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

/**
 * Common for Characters and SearchScreen
 */
@Composable
fun LoadedState(
    charactersResult: List<Character>,
    isSuccess: Boolean,
    errorText: String,
    onCharacterClick: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        OutdatedDataBanner(show = !isSuccess, errorText = errorText)
        Characters(
            characters = charactersResult,
            onCharacterClicked = onCharacterClick
        )
    }
}

/**
 * Common for Characters and SearchScreen
 */
@Composable
private fun OutdatedDataBanner(show: Boolean, errorText: String) {
    if (show) {
        Text(
            text = errorText,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.errorContainer)
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

/**
 * Common for Characters, Favorites and SearchScreen
 */
@Composable
fun Characters(
    characters: List<Character>,
    onCharacterClicked: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(characters, key = { it.id }) { character ->
            CharacterListItem(
                character = character,
                onCharacterClicked = onCharacterClicked,
            )
        }
    }
}

/**
 * Common for Characters, Favorites and SearchScreen
 */
@Composable
fun CharacterListItem(
    character: Character,
    onCharacterClicked: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .clickable {
                onCharacterClicked(character.id)
            },
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CharacterCardContent(character = character)
        }
    }
}

/**
 * Common for Characters, Favorites and SearchScreen
 */
@Composable
fun CharacterCardContent(character: Character) {
    AsyncImage(
        model = character.imageUrl,
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentDescription = stringResource(id = R.string.avatarWithName, character.name)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
    ) {
        Row {
            Text(text = character.name, style = MaterialTheme.typography.titleSmall)
            FavoriteIconIndicator(character.isFavorite)
        }
        Text(text = character.status, style = MaterialTheme.typography.displayMedium)
    }
}