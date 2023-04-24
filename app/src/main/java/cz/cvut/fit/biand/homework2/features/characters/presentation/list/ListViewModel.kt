package cz.cvut.fit.biand.homework2.features.characters.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.CharactersResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow<ListScreenState>(ListScreenState.Loading)
    val screenStateStream get() = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            val result = characterRepository.getCharacters()
            _screenStateStream.value = ListScreenState.Loaded(result)
        }
    }
}

sealed interface ListScreenState {
    object Loading : ListScreenState

    data class Loaded(val charactersResult: CharactersResult) : ListScreenState
}