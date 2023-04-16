package cz.cvut.fit.biand.homework2.system

import androidx.compose.foundation.Image
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.model.Character
import cz.cvut.fit.biand.homework2.presentation.DetailViewModel
import cz.cvut.fit.biand.homework2.ui.theme.Blue
import cz.cvut.fit.biand.homework2.ui.theme.Gray

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = DetailViewModel(),
    id: Int?,
) {
    val character by viewModel.character.collectAsState()
    val favorite by viewModel.favorite.collectAsState()
    LaunchedEffect(Unit) {
        id?.let {
            viewModel.getCharacter(id)
        }
    }

    DetailScreenContent(
        character = character,
        favorite = favorite,
        onFavorite = viewModel::onFavoriteClick,
        onNavigateBack = { navController.popBackStack() },
    )
}

@Composable
fun DetailScreenContent(
    character: Character?,
    favorite: Boolean,
    onFavorite: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    character?.let {
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
                        Text(text = character.name)
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
                                tint = Blue,
                                contentDescription = "Favourite navigation icon",
                            )
                        }
                    },
                )
            },
        ) {
            Column(modifier = Modifier.padding(it)) {
                CharacterDetail(
                    character,
                )
            }
        }
    }
}

@Composable
fun CharacterDetail(character: Character) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(all = 16.dp)) {
            Image(
                painter = painterResource(character.imageRes),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(150.dp),
                contentDescription = "Image",
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
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = character.name,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Divider(
            color = Gray,
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
            Information(title = stringResource(R.string.status), value = character.status)
            Information(title = stringResource(R.string.species), value = character.species)
            Information(title = stringResource(R.string.type), value = character.type)
            Information(title = stringResource(R.string.gender), value = character.gender)
            Information(title = stringResource(R.string.origin), value = character.origin)
            Information(title = stringResource(R.string.location), value = character.location)
        }
    }
}

@Composable
fun Information(title: String, value: String) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.5F),
            text = value,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}
