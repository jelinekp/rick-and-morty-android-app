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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.data.CharactersResult
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.FavoriteIconIndicator

/**
 * Common for Characters and SearchScreen
 */
@Composable
fun LoadedState(charactersResult: CharactersResult, errorText: String, onCharacterClick: (DbCharacter) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        OutdatedDataBanner(show = !charactersResult.isSuccess, errorText = errorText)
        Characters(characters = charactersResult.characters.collectAsStateWithLifecycle(initialValue = emptyList()).value, onCharacterClicked = onCharacterClick)
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
    characters: List<DbCharacter>,
    onCharacterClicked: (DbCharacter) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(characters, key = { it.id }) { character ->
            CharacterListItem(
                dbCharacter = character,
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
    dbCharacter: DbCharacter,
    onCharacterClicked: (DbCharacter) -> Unit,
) {
    Card(
        modifier = Modifier
            .clickable {
                onCharacterClicked(dbCharacter)
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
            CharacterCardContent(dbCharacter = dbCharacter)
        }
    }
}

/**
 * Common for Characters, Favorites and SearchScreen
 */
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
            FavoriteIconIndicator(dbCharacter.isFavorite)
        }
        Text(text = dbCharacter.status, style = MaterialTheme.typography.displayMedium)
    }
}