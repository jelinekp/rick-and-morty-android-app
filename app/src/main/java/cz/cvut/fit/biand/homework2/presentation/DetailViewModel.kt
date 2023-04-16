package cz.cvut.fit.biand.homework2.presentation

import androidx.lifecycle.ViewModel
import cz.cvut.fit.biand.homework2.data.CharactersDataSource
import cz.cvut.fit.biand.homework2.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailViewModel : ViewModel() {
    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character

    val favorite: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun getCharacter(id: Int) {
        _character.value = CharactersDataSource.getCharacter(id)
    }

    fun onFavoriteClick() {
        favorite.value = !favorite.value
    }
}
