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
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.BackIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    viewModel: CharacterDetailViewModel = koinViewModel(),
    navigateUp: () -> Unit,
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    
    DetailScreen(
        dbCharacter = screenState.character,
        onNavigateBack = navigateUp,
        onFavorite = viewModel::onFavoriteClick
    )
}

@Composable
private fun DetailScreen(
    dbCharacter: DbCharacter?,
    onNavigateBack: () -> Unit,
    onFavorite: () -> Unit = {},
) {
    dbCharacter?.let {
        Scaffold(
            topBar = {
                DetailTopBar(title = dbCharacter.name, favorite = dbCharacter.isFavorite, onNavigateBack = onNavigateBack, onFavorite = onFavorite)
            },
            backgroundColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            CharacterDetailCard(dbCharacter, paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun DetailTopBar(
    title: String?,
    favorite: Boolean,
    onFavorite: () -> Unit,
    onNavigateBack: () -> Unit,
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
        navigationIcon = { BackIcon(onNavigateBack) },
        actions = {
            FavoriteIcon(favoriteIconAction = onFavorite, isFavorite = favorite)
        },
    )
}

@Composable private fun FavoriteIcon(
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
private fun CharacterDetailCard(dbCharacter: DbCharacter, paddingValues: PaddingValues) {
    Card(
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState(0)) // important to add verticalScroll() before padding!
            .padding(paddingValues)
            .padding(all = 8.dp)
    ) {
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()) {
            Row {
                AsyncImage(
                    model = dbCharacter.imageUrl,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentDescription = stringResource(R.string.image)
                )
                CharacterTitle(title = dbCharacter.name)
            }
            Divider()
            Column(modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)) {
                Information(title = stringResource(R.string.status), value = dbCharacter.status)
                Information(title = stringResource(R.string.species), value = dbCharacter.species)
                Information(title = stringResource(R.string.type), value = dbCharacter.type)
                Information(title = stringResource(R.string.gender), value = dbCharacter.gender)
                Information(title = stringResource(R.string.origin), value = dbCharacter.origin)
                Information(title = stringResource(R.string.location), value = dbCharacter.location)
            }
        }
    }
}

@Composable
fun CharacterTitle(
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
            modifier = Modifier.padding(top = 14.dp).fillMaxWidth(0.7F)
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
