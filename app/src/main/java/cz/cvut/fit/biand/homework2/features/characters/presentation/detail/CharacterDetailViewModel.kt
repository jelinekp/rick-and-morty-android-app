package cz.cvut.fit.biand.homework2.features.characters.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.navigation.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(CharacterDetailScreenState())
    val screenStateStream get() = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            val characterId: Int = savedStateHandle[Screens.DetailScreen.ID] ?: throw NullPointerException("Character is missing")
            val character = characterRepository.getCharacter(characterId)
            _screenStateStream.update {
                it.copy(character = character)
            }
        }
    }

    val favorite: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun onFavoriteClick() {
        favorite.value = !favorite.value
    }
}

data class CharacterDetailScreenState(val character: DbCharacter? = null)