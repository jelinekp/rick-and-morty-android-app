package cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.FavoriteIconIndicator
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadingState
import org.koin.androidx.compose.koinViewModel

/**
 * Screen representing a list of characters for "Characters" and "Favorites"
 */
@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navigateToCharacterDetail: (id: String) -> Unit,
    paddingValues: PaddingValues
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    ListScreen(
        screenState = screenState,
        navigateToCharacterDetail = navigateToCharacterDetail,
        paddingValues = paddingValues
    )
}

@Composable
private fun ListScreen(
    screenState: ListScreenState,
    navigateToCharacterDetail: (id: String) -> Unit,
    paddingValues: PaddingValues
) {
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
                errorText = stringResource(R.string.outdated_data_message),
                onCharacterClick = navigateToCharacterDetail
            )
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

/**
 * Common for Characters and SearchScreen
 */
@Composable
fun LoadedState(
    charactersResult: List<Character>,
    isSuccess: Boolean,
    errorText: String,
    onCharacterClick: (String) -> Unit,
    state: LazyListState = LazyListState(),
) {
    Column(Modifier.fillMaxSize()) {
        OutdatedDataBanner(show = !isSuccess, errorText = errorText)
        Characters(
            characters = charactersResult,
            onCharacterClicked = onCharacterClick,
            state = state,
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
    state: LazyListState = LazyListState()
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = state
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
            Text(text = character.name, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(7f, fill = false))
            FavoriteIconIndicator(isFavorite = character.isFavorite, modifier = Modifier.weight(1f))
        }
        Text(text = character.status, style = MaterialTheme.typography.displayMedium)
    }
}