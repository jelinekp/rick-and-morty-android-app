package cz.cvut.fit.biand.homework2.system

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.model.Character
import cz.cvut.fit.biand.homework2.navigation.Screen
import cz.cvut.fit.biand.homework2.presentation.ListViewModel
import cz.cvut.fit.biand.homework2.ui.theme.Blue

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = ListViewModel(),
) {
    val characters by viewModel.characters.collectAsState()
    ListScreenContent(
        characters = characters,
        onSearchClicked = {
            navController.navigate(Screen.SearchScreen.route)
        },
        onCharacterClicked = {
            navController.navigate(Screen.DetailScreen.route + "/$it")
        },
    )
}

@Composable
fun ListScreenContent(
    characters: List<Character>,
    onSearchClicked: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
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
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            items(characters) { character ->
                CharacterListItem(
                    character = character,
                    onCharacterClicked = onCharacterClicked,
                )
            }
        }
    }
}

@Composable
fun BottomBar() {
    BottomNavigation() {
        BottomNavigationItem(
            label = {
                Text(
                    text = stringResource(id = R.string.characters),
                    color = Blue,
                )
            },
            onClick = {},
            selected = true,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_characters),
                    tint = Blue,
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
    character: Character,
    onCharacterClicked: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clickable {
                onCharacterClicked(character.id)
            },
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
        ) {
            Image(
                painter = painterResource(character.imageRes),
                contentDescription = "Character avatar",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(64.dp),
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
                        text = character.name,
                        style = MaterialTheme.typography.h6,
                    )
                }
                Text(text = character.status)
            }
        }
    }
}
