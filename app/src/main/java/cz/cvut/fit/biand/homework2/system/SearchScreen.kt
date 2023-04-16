package cz.cvut.fit.biand.homework2.system

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.model.Character
import cz.cvut.fit.biand.homework2.navigation.Screen
import cz.cvut.fit.biand.homework2.presentation.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = SearchViewModel(),
) {
    val characters by viewModel.characters.collectAsState()
    val searchedText by viewModel.searchText.collectAsState()

    SearchScreenContent(
        onNavigateBack = { navController.popBackStack() },
        searchedText = searchedText,
        characters = characters,
        onSearch = viewModel::searchCharacters,
        onClear = viewModel::clearText,
        onCharacterClicked = {
            navController.navigate(Screen.DetailScreen.route + "/$it")
        },
    )
}

@Composable
fun SearchScreenContent(
    searchedText: String,
    characters: List<Character>,
    onNavigateBack: () -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
) {
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
                    SearchTopBarTitle(
                        searchedText,
                        onSearch,
                        onClear,
                    )
                },
            )
        },
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
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
private fun SearchTopBarTitle(
    text: String,
    onSearchTextChanged: (String) -> Unit,
    onClear: () -> Unit,
) {
    var showClearButton by remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            },
        value = text,
        onValueChange = onSearchTextChanged,
        placeholder = {
            Text(text = stringResource(R.string.search))
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
        ),
        trailingIcon = {
            IconButton(onClick = {
                onClear()
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear icon",
                )
            }
        },
    )
}
