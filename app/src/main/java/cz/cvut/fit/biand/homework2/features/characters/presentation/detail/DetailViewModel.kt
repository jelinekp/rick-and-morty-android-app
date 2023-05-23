package cz.cvut.fit.biand.homework2.features.characters.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.navigation.Screens
import kotlinx.coroutines.Dispatchers
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
            val characterId: String = savedStateHandle[Screens.DetailScreen.ID]
                ?: throw NullPointerException("Character is missing")
            val character = characterRepository.getCharacter(characterId)
            character.collect { dbCharacter ->
                _screenStateStream.update { screenStateStream ->
                    // Log.d("Updated Character", dbCharacter.toString())
                    screenStateStream.copy(character = dbCharacter)
                }
            }
        }
    }

    fun onFavoriteClick() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenStateStream.value.character?.let {
                characterRepository.updateFavorite(it.id, !it.isFavorite)
            }
        }
    }
}

data class CharacterDetailScreenState(val character: DbCharacter? = null)