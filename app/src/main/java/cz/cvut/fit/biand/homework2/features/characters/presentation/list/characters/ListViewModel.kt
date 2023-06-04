package cz.cvut.fit.biand.homework2.features.characters.presentation.list.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow<ListScreenState>(ListScreenState.Loading)
    val screenStateStream get() = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            val isSuccessfullyLoaded = characterRepository.getCharacters().isSuccess
            characterRepository.getCharacters().characters.collectLatest {
                _screenStateStream.value =
                    ListScreenState.Loaded(charactersResult = it, isSuccess = isSuccessfullyLoaded)
            }
        }
    }
}

sealed interface ListScreenState {
    object Loading : ListScreenState

    data class Loaded(
        val charactersResult: List<Character>,
        val isSuccess: Boolean,
    ) : ListScreenState

}
