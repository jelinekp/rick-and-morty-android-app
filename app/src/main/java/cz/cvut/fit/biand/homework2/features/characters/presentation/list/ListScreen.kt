package cz.cvut.fit.biand.homework2.features.characters.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import cz.cvut.fit.biand.homework2.features.characters.data.CharactersResult
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
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
    ListScreenContent(
        screenState = screenState,
        onSearchClicked = navigateToSearch,
        navigateToCharacterDetail = navigateToCharacterDetail,
    )
}

@Composable
fun ListScreenContent(
    screenState: ListScreenState,
    onSearchClicked: () -> Unit,
    navigateToCharacterDetail: (id: String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = stringResource(id = R.string.characters))
                        Icon(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 24.dp)
                                .clickable {
                                    onSearchClicked()
                                },
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search",
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomBar()
        },
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
                    onCharacterClick = { navigateToCharacterDetail(it.id) }
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadedState(charactersResult: CharactersResult, onCharacterClick: (DbCharacter) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        OutdatedDataBanner(show = !charactersResult.isSuccess)
        Characters(characters = charactersResult.characters, onCharacterClicked = onCharacterClick)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Characters(
    characters: List<DbCharacter>,
    onCharacterClicked: (DbCharacter) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 8.dp)
    ) {
        items(characters, key = { it.id }) { character ->
            CharacterListItem(
                dbCharacter = character,
                onCharacterClicked = onCharacterClicked,
            )
        }
    }
}

@Composable
private fun OutdatedDataBanner(show: Boolean) {
    if (show) {
        androidx.compose.material3.Text(
            text = stringResource(R.string.outdated_data_message),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.errorContainer)
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
fun BottomBar() {
    BottomNavigation() {
        BottomNavigationItem(
            label = {
                Text(
                    text = stringResource(id = R.string.characters),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            },
            onClick = {},
            selected = true,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_characters),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = "Favourite navigation icon",
                )
            },
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
                    contentDescription = "Favourite navigation icon",
                )
            },
        )
    }
}

@Composable
fun CharacterListItem(
    dbCharacter: DbCharacter,
    onCharacterClicked: (DbCharacter) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clickable {
                onCharacterClicked(dbCharacter)
            },
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
        ) {
            AsyncImage(
                model = dbCharacter.imageUrl,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(64.dp),
                contentDescription = "Character avatar"
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = dbCharacter.name,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                Text(text = dbCharacter.status)
            }
        }
    }
}
