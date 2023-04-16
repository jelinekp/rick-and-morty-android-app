package cz.cvut.fit.biand.homework2.presentation

import androidx.lifecycle.ViewModel
import cz.cvut.fit.biand.homework2.data.CharactersDataSource
import cz.cvut.fit.biand.homework2.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListViewModel : ViewModel() {
    private val _characters = MutableStateFlow(CharactersDataSource.getAllCharacters())
    val characters: StateFlow<List<Character>> = _characters
}
