package cz.cvut.fit.biand.homework2.features.characters.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.CharactersResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val screenStateStream get() = _screenStateStream.asStateFlow()

    init {
        getAllCharacters()
    }

    private fun getAllCharacters() {
        viewModelScope.launch {
            val result = characterRepository.getCharacters()
            _screenStateStream.value = SearchScreenState.Loaded(result)
        }
    }

    val searchText: MutableStateFlow<String> = MutableStateFlow("")

    fun searchCharacters(name: String) {
        searchText.value = name
        viewModelScope.launch {
            characterRepository.getApiCharactersByName(name.lowercase())
            if (name.isBlank()) {
                _screenStateStream.value = _screenStateStream.value
            } else {
                _screenStateStream.value = SearchScreenState.Loaded(characterRepository.getApiCharactersByName(name))
            }
        }
    }

    fun clearText() {
        getAllCharacters()
        searchText.value = ""
    }
}

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    data class Loaded(
        val charactersResult: CharactersResult,
        ) : SearchScreenState
}

