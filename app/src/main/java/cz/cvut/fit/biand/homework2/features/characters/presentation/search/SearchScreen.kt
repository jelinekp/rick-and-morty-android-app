package cz.cvut.fit.biand.homework2.features.characters.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.BackIcon
import cz.cvut.fit.biand.homework2.features.characters.presentation.common.LoadingState
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters.LoadedState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    navigateToCharacterDetail: (id: String) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val searchedText by viewModel.searchTextFieldValue.collectAsStateWithLifecycle()
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val textFieldFocus by viewModel.textFieldFocus.collectAsStateWithLifecycle()
    val lazyColumnScrollState by viewModel.lazyColumnScrollState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // Important to refresh search result on each search screen entry to avoid displaying all data (Favorites Icon can change)
        viewModel.refreshSearchResults()
    }

    SearchScreenContent(
        onNavigateBack = navigateUp,
        searchedText = searchedText,
        screenState = screenState,
        onSearch = viewModel::searchCharacters,
        onClear = viewModel::clearText,
        onCharacterClicked = navigateToCharacterDetail,
        searchFieldFocusRequester = textFieldFocus,
        lazyColumnScrollState = lazyColumnScrollState,
    )
}

@Composable
private fun SearchScreenContent(
    searchedText: TextFieldValue,
    screenState: SearchScreenState,
    onNavigateBack: () -> Unit,
    onSearch: (TextFieldValue) -> Unit,
    onClear: () -> Unit,
    onCharacterClicked: (id: String) -> Unit = {},
    searchFieldFocusRequester: FocusRequester,
    lazyColumnScrollState: LazyListState,
) {
    LaunchedEffect(Unit) {
        // automatically showing up sw keyboard
        searchFieldFocusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 10.dp,
                backgroundColor = MaterialTheme.colorScheme.background,
                navigationIcon = { BackIcon(onNavigateBack) },
                title = {
                    SearchTopBarTitle(
                        textFieldValue = searchedText,
                        onSearchTextChanged = onSearch,
                        onClear = onClear,
                        focusRequester = searchFieldFocusRequester,
                    )
                },
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background
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
                    isSuccess = screenState.isSuccess,
                    errorText = if (searchedText.text.isEmpty())
                        stringResource(R.string.please_connect_your_device_to_internet_to_perform_search)
                    else stringResource(id = R.string.noCharactersFoundForQuery, searchedText.text),
                    onCharacterClick = { onCharacterClicked(it) },
                    state = lazyColumnScrollState,
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTopBarTitle(
    textFieldValue: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester
) {
    var showClearButton by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            }
            .focusRequester(focusRequester = focusRequester),
        value = textFieldValue,
        onValueChange = onSearchTextChanged,
        placeholder = {
            Box {
                Text(
                    text = stringResource(R.string.search),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            focusRequester.freeFocus()
            keyboardController?.hide()
        }),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            textColor = MaterialTheme.colorScheme.onBackground
        ),
        trailingIcon = {
            IconButton(onClick = {
                if (textFieldValue.text.isBlank()) {
                    focusRequester.freeFocus()
                    keyboardController?.hide()
                }
                onClear()
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
    )
}
