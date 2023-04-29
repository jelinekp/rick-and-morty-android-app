package cz.cvut.fit.biand.homework2.features.characters.presentation.list.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters.Characters

@Composable
fun FavoriteCharactersList(
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