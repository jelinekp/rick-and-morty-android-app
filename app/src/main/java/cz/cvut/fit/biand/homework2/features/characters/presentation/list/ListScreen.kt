package cz.cvut.fit.biand.homework2.features.characters.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
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
import coil.compose.AsyncImage
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadedState
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadingState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateToSearch: () -> Unit,
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    ListScreen(
        screenState = screenState,
        navigateToCharacterDetail = navigateToCharacterDetail,
        navigateToSearch = navigateToSearch,
    )
}

@Composable
private fun ListScreen(
    screenState: ListScreenState,
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateToSearch: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            ListTopBar(
                title = stringResource(id = R.string.characters),
                searchIconAction = navigateToSearch,
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
        bottomBar = {
            BottomBar()
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
                    errorText = stringResource(R.string.outdated_data_message),
                    onCharacterClick = { navigateToCharacterDetail(it.id) }
                )
            }
        }
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
        //modifier = Modifier.height(48.dp)
    )
}

@Composable
private fun BottomBar() {
    BottomNavigation(
        modifier = Modifier
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        elevation = 4.dp
    ) {
        BottomNavigationItem(
            label = {
                Text(text = stringResource(id = R.string.characters), style = MaterialTheme.typography.bodyMedium)
            },
            onClick = {},
            selected = true,
            icon = { Icon(
                    painter = painterResource(id = R.drawable.ic_characters),
                    contentDescription = stringResource(R.string.characters_navigation_icon),
                ) },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.outline
        )
        BottomNavigationItem(
            label = {
                Text(
                    text = stringResource(id = R.string.favorites),
                )
            },
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorites_filled),
                    contentDescription = stringResource(R.string.favourite_navigation_icon),
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.outline
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListItem(
    dbCharacter: DbCharacter,
    onCharacterClicked: (DbCharacter) -> Unit,
) {
    Card(
        modifier = Modifier
            .clickable {
                onCharacterClicked(dbCharacter)
            },
        elevation = cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CharacterCardContent(dbCharacter = dbCharacter)
        }
    }
}

@Composable
fun CharacterCardContent(dbCharacter: DbCharacter) {
    AsyncImage(
        model = dbCharacter.imageUrl,
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentDescription = stringResource(id = R.string.avatarWithName,dbCharacter.name)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
    ) {
        Row {
            Text(text = dbCharacter.name, style = MaterialTheme.typography.titleSmall)
            FavoriteIcon(dbCharacter.isFavorite)
        }
        Text(text = dbCharacter.status, style = MaterialTheme.typography.displayMedium)
    }
}

@Composable
fun FavoriteIcon(
    isFavorite: Boolean
) {
    if (isFavorite) {
        Icon(
            painter = painterResource(id = R.drawable.ic_favorites_filled),
            contentDescription = stringResource(id = R.string.characterIsInFavorites),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 5.dp, top = 5.dp)
                .size(14.dp)
        )
    }
}
