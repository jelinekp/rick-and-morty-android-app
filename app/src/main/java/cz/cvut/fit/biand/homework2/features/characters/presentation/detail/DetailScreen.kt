package cz.cvut.fit.biand.homework2.features.characters.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.BackIcon
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadingState
import org.koin.androidx.compose.koinViewModel
import cz.cvut.fit.biand.homework2.features.characters.model.Character

@Composable
fun DetailScreen(
    viewModel: CharacterDetailViewModel = koinViewModel(),
    navigateUp: () -> Unit,
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    DetailScreen(
        character = screenState.character,
        onNavigateBack = navigateUp,
        onFavorite = viewModel::onFavoriteClick
    )
}

@Composable
private fun DetailScreen(
    character: Character?,
    onNavigateBack: () -> Unit,
    onFavorite: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            DetailTopBar(
                title = character?.name ?: "",
                favorite = character?.isFavorite ?: false,
                onNavigateBack = onNavigateBack,
                onFavorite = onFavorite
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (character != null)
            CharacterDetailCard(character, paddingValues)
        else {
            LoadingState()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopBar(
    title: String?,
    favorite: Boolean,
    onFavorite: () -> Unit,
    onNavigateBack: () -> Unit,
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
        navigationIcon = { BackIcon(onNavigateBack) },
        actions = {
            FavoriteIconButton(favoriteIconAction = onFavorite, isFavorite = favorite)
        },
        modifier = modifier
    )
}

@Composable
private fun FavoriteIconButton(
    favoriteIconAction: () -> Unit,
    isFavorite: Boolean
) {
    IconButton(onClick = favoriteIconAction) {
        if (isFavorite) {
            Icon(
                painter = painterResource(id = R.drawable.ic_favorites_filled),
                contentDescription = stringResource(R.string.characterIsInFavorites),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_favorites),
                contentDescription = stringResource(R.string.characterIsNotInFavorites),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun CharacterDetailCard(character: Character, paddingValues: PaddingValues) {
    Card(
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState(0)) // important to add verticalScroll() before padding!
            .padding(paddingValues)
            .padding(all = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize()
        ) {
            Row {
                AsyncImage(
                    model = character.imageUrl,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentDescription = stringResource(R.string.image),
                    error = painterResource(id = R.drawable.ic_launcher_foreground)
                )
                CharacterTitle(title = character.name)
            }
            Divider()
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                Information(title = stringResource(R.string.status), value = character.status)
                Information(title = stringResource(R.string.species), value = character.species)
                Information(title = stringResource(R.string.type), value = character.type)
                Information(title = stringResource(R.string.gender), value = character.gender)
                Information(title = stringResource(R.string.origin), value = character.origin)
                Information(title = stringResource(R.string.location), value = character.location)
            }
        }
    }
}

@Composable
private fun CharacterTitle(
    title: String
) {
    Column {
        Text(
            text = stringResource(R.string.name),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 17.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 14.dp)
                .fillMaxWidth(0.7F)
        )
    }
}

@Composable
private fun Information(title: String, value: String) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.5F),
            text = value.ifEmpty { "-" },
            style = MaterialTheme.typography.titleSmall,
        )
    }
}
