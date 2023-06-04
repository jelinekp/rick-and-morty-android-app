package cz.cvut.fit.biand.homework2.features.characters.presentation.list.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters.Characters
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel(),
    navigateToCharacterDetail: (id: String) -> Unit,
    paddingValues: PaddingValues
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    FavoritesScreen(
        screenState = screenState,
        navigateToCharacterDetail = navigateToCharacterDetail,
        paddingValues = paddingValues,
    )
}

@Composable
private fun FavoritesScreen(
    screenState: FavoritesScreenState,
    navigateToCharacterDetail: (id: String) -> Unit,
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(paddingValues = paddingValues).fillMaxSize()
    ) {
        if (screenState.favoriteCharacters.isEmpty()) {
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
                characters = screenState.favoriteCharacters,
                onCharacterClicked = navigateToCharacterDetail,
            )
        }
    }
}