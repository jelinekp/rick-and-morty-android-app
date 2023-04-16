package cz.cvut.fit.biand.homework2.presentation

import androidx.lifecycle.ViewModel
import cz.cvut.fit.biand.homework2.data.CharactersDataSource
import cz.cvut.fit.biand.homework2.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel : ViewModel() {
    private val _allCharacters = MutableStateFlow(CharactersDataSource.getAllCharacters())

    private val _characters = MutableStateFlow(CharactersDataSource.getAllCharacters())
    val characters: StateFlow<List<Character>> = _characters

    val searchText: MutableStateFlow<String> = MutableStateFlow("")

    fun searchCharacters(name: String) {
        searchText.value = name
        if (name.isBlank()) {
            _characters.value = _allCharacters.value
        } else {
            _characters.value = _allCharacters.value.filter { character ->
                character.name.lowercase().contains(name.lowercase())
            }
        }
    }

    fun clearText() {
        _characters.value = _allCharacters.value
        searchText.value = ""
    }
}
