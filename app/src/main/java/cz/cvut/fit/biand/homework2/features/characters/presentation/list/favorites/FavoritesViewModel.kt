package cz.cvut.fit.biand.homework2.features.characters.presentation.list.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(FavoritesScreenState(
        emptyList()
    ))
    val screenStateStream get() = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            characterRepository.getFavoriteCharacters().collectLatest {
                _screenStateStream.value = _screenStateStream.value.copy(favoriteCharacters = it)
            }
        }
    }

}

data class FavoritesScreenState(
    val favoriteCharacters: List<Character>
)