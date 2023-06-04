package cz.cvut.fit.biand.homework2.features.characters.presentation.search

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.domain.GetSearchResultsUseCase
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val screenStateStream get() = _screenStateStream.asStateFlow()

    private val _searchTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val searchTextFieldValue get() = _searchTextFieldValue.asStateFlow()

    private val _textFieldFocus = MutableStateFlow(FocusRequester())
    val textFieldFocus get() = _textFieldFocus.asStateFlow()

    private val _lazyColumnScrollState = MutableStateFlow(LazyListState())
    val lazyColumnScrollState get() = _lazyColumnScrollState.asStateFlow()

    fun searchCharacters(name: TextFieldValue) {
        val prevSearchTextValue = _searchTextFieldValue.value.text
        _searchTextFieldValue.update {
            name
        }
        viewModelScope.launch {
            val characterSearchResult = getSearchResultsUseCase(query = name.text)
            _screenStateStream.value = SearchScreenState.Loaded(
                charactersResult = characterSearchResult.characters,
                isSuccess = characterSearchResult.isSuccess
            )
        }

        if (prevSearchTextValue.length > name.text.length)
            scrollUp()

    }

    private fun getAllCharacters() = searchCharacters(TextFieldValue(""))

    private fun scrollUp() {
        viewModelScope.launch {
            // Delay: need to wait for the content recomposition - this is a hot fix but working 90% of time:
            delay(250)
            _lazyColumnScrollState.value.scrollToItem(0)
        }
    }

    fun clearText() {
        _searchTextFieldValue.update {
            TextFieldValue("")
        }
        getAllCharacters()
        scrollUp()
    }

    fun refreshSearchResults() = searchCharacters(_searchTextFieldValue.value)
}

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    data class Loaded(
        val charactersResult: List<Character>,
        val isSuccess: Boolean,
    ) : SearchScreenState
}

