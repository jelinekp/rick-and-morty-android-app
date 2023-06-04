package cz.cvut.fit.biand.homework2.features.characters.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val screenStateStream get() = _screenStateStream.asStateFlow()

    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")
    val searchText get() = _searchText.asStateFlow()

    init {
        getAllCharacters()
    }

    private fun getAllCharacters() {
        viewModelScope.launch {
            // TODO stop collecting after name is not blank!
            characterRepository.getCharacters().characters.first {
                _screenStateStream.value = SearchScreenState.Loaded(charactersResult = it, isSuccess = true)
                true
            }
        }
    }

    fun searchCharacters(name: String) {
        _searchText.value = name
        if (name.isBlank()) {
            getAllCharacters()
        } else {
            viewModelScope.launch {
                val characterSearchResult = characterRepository.getApiCharactersByName(name)
                _screenStateStream.value = SearchScreenState.Loaded(
                    charactersResult = characterSearchResult.characters,
                    isSuccess = characterSearchResult.isSuccess
                )
            }
        }
    }

    fun clearText() {
        getAllCharacters()
        _searchText.value = ""
    }
}

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    data class Loaded(
        val charactersResult: List<Character>,
        val isSuccess: Boolean,
    ) : SearchScreenState
}

