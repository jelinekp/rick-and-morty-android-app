package cz.cvut.fit.biand.homework2.features.characters.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.data.CharactersResult
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.CharacterListItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadedState(charactersResult: CharactersResult, errorText: String, onCharacterClick: (DbCharacter) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        OutdatedDataBanner(show = !charactersResult.isSuccess, errorText = errorText)
        Characters(characters = charactersResult.characters.collectAsStateWithLifecycle(initialValue = emptyList()).value, onCharacterClicked = onCharacterClick)
    }
}

@Composable
private fun OutdatedDataBanner(show: Boolean, errorText: String) {
    if (show) {
        androidx.compose.material3.Text(
            text = errorText,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.errorContainer)
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Characters(
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
 * Top left back icon and its action handling
 */
@Composable
fun BackIcon(
    backIconAction: () -> Unit
) {
    IconButton(onClick = backIconAction) {
        Icon(
            painter = painterResource(id = R.drawable._arrow_back_),
            contentDescription = stringResource(R.string.backIcon),
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}