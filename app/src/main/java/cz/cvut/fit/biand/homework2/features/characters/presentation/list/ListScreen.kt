package cz.cvut.fit.biand.homework2.features.characters.presentation.list

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.Characters
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadedState
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadingState
import cz.cvut.fit.biand.homework2.navigation.BottomBarScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Screen representing a list of characters
 */
@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateToSearch: () -> Unit,
    onBottomNavItemClick: (String) -> Unit,
    currentScreen: BottomBarScreen,
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    ListScreen(
        screenState = screenState,
        navigateToCharacterDetail = navigateToCharacterDetail,
        navigateToSearch = navigateToSearch,
        onBottomNavItemClick = onBottomNavItemClick,
        currentScreen = currentScreen
    )
}

@Composable
private fun ListScreen(
    screenState: ListScreenState,
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateToSearch: () -> Unit,
    onBottomNavItemClick: (String) -> Unit,
    currentScreen: BottomBarScreen,
) {
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            ListTopBar(
                title = stringResource(id = currentScreen.title),
                searchIconAction = navigateToSearch,
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = currentScreen,
                onItemClick = onBottomNavItemClick
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
                is ListScreenState.Loaded ->
                    if (currentScreen == BottomBarScreen.Characters) {
                        LoadedState(
                            charactersResult = screenState.charactersResult,
                            errorText = stringResource(R.string.outdated_data_message),
                            onCharacterClick = { navigateToCharacterDetail(it.id) }
                        )
                    } else {
                        FavoriteCharactersList(
                            favoriteCharacters = screenState.favoriteCharacters.collectAsStateWithLifecycle(initialValue = emptyList()).value,
                            onCharacterClicked = { navigateToCharacterDetail(it.id) },
                        )
                    }
            }
        }
    }
}

@Composable fun FavoriteCharactersList(
    favoriteCharacters: List<DbCharacter>,
    onCharacterClicked: (DbCharacter) -> Unit
) {
    if (favoriteCharacters.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.no_favorite_characters_yet),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    } else {
        Characters(
            characters = favoriteCharacters,
            onCharacterClicked = onCharacterClicked,
        )
    }
}

@Composable
fun ListTopBar(
    title: String?,
    searchIconAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colorScheme.background,
        title = { Text(
            text = title ?: "",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        ) },
        navigationIcon = null,
        actions = { IconButton(onClick = searchIconAction) {
            Icon(
                painter =  painterResource(id = R.drawable.ic_search),
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
    currentScreen: BottomBarScreen,
    onItemClick: (String) -> Unit
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
                currentScreen = currentScreen,
                modifier = Modifier.weight(4f),
                onItemClick = onItemClick
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun RowScope.BottomBarItem(
    screen: BottomBarScreen,
    currentScreen: BottomBarScreen,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    BottomNavigationItem(
        modifier = modifier,
        icon = { Icon(
            painter = painterResource(id = screen.icon),
            contentDescription = stringResource(R.string.navigation_icon)
        ) },
        selected = currentScreen == screen,
        unselectedContentColor = MaterialTheme.colorScheme.outline,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        label = { Text(text = stringResource(id = screen.title), style = MaterialTheme.typography.bodyMedium) },
        alwaysShowLabel = true,
        onClick = { onItemClick(screen.route) }
    )
}
