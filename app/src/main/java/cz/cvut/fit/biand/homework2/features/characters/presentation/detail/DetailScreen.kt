package cz.cvut.fit.biand.homework2.features.characters.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    viewModel: CharacterDetailViewModel = koinViewModel(),
    navigateUp: () -> Unit,
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    
    DetailScreen(screenState = screenState, navigateUp = navigateUp)
}
@Composable
private fun DetailScreen(
    screenState: CharacterDetailScreenState,
    navigateUp: () -> Unit,
) {
    DetailScreenContent(
        dbCharacter = screenState.character,
        onNavigateBack = navigateUp,
        /*favorite = favorite,
        onFavorite = viewModel::onFavoriteClick,*/
    )
}

@Composable
private fun DetailScreenContent(
    dbCharacter: DbCharacter?,
    onNavigateBack: () -> Unit,
    favorite: Boolean = false,
    onFavorite: () -> Unit = {},
) {
    dbCharacter?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Arrow back",
                            )
                        }
                    },
                    title = {
                        Text(text = dbCharacter.name)
                    },
                    actions = {
                        IconButton(onClick = onFavorite) {
                            Icon(
                                painter = if (favorite) {
                                    painterResource(id = R.drawable.ic_favorites_filled)
                                } else {
                                    painterResource(
                                        id = R.drawable.ic_favorites,
                                    )
                                },
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                contentDescription = "Favourite navigation icon",
                            )
                        }
                    },
                )
            },
        ) {
            Column(modifier = Modifier.padding(it)) {
                CharacterDetail(
                    dbCharacter,
                )
            }
        }
    }
}

@Composable
private fun CharacterDetail(dbCharacter: DbCharacter) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(all = 16.dp)) {
            AsyncImage(
                model = dbCharacter.imageUrl,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(150.dp),
                contentDescription = "Image"
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        bottom = 8.dp,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.name),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = dbCharacter.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Divider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .width(1.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        ) {
            Information(title = stringResource(R.string.status), value = dbCharacter.status)
            Information(title = stringResource(R.string.species), value = dbCharacter.species)
            Information(title = stringResource(R.string.type), value = dbCharacter.type)
            Information(title = stringResource(R.string.gender), value = dbCharacter.gender)
            Information(title = stringResource(R.string.origin), value = dbCharacter.origin)
            Information(title = stringResource(R.string.location), value = dbCharacter.location)
        }
    }
}


@Composable
private fun Information(title: String, value: String) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.5F),
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}
