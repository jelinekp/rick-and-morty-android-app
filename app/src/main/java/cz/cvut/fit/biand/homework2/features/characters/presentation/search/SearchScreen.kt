package cz.cvut.fit.biand.homework2.features.characters.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.LoadedState
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.LoadingState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val searchedText by viewModel.searchText.collectAsState()
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    SearchScreenContent(
        onNavigateBack = navigateUp,
        searchedText = searchedText,
        screenState = screenState,
        onSearch = viewModel::searchCharacters,
        onClear = viewModel::clearText,
        onCharacterClicked = navigateToCharacterDetail,
    )
}

@Composable
private fun SearchScreenContent(
    searchedText: String,
    screenState: SearchScreenState,
    onNavigateBack: () -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onCharacterClicked: (id: String) -> Unit = {},
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (screenState) {
                is SearchScreenState.Loading -> LoadingState()
                is SearchScreenState.Loaded -> LoadedState(
                    charactersResult = screenState.charactersResult,
                    onCharacterClick = { onCharacterClicked(it.id) }
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
