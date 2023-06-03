package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {

    fun getCharacters(): Flow<List<Character>>

    fun getFavoriteCharacters(): Flow<List<Character>>

    fun getCharacter(id: String): Flow<Character?>

    suspend fun isCharacterFavorite(id: String): Boolean

    suspend fun updateFavorite(character: Character, isFavorite: Boolean)

    suspend fun insert(characters: List<Character>)

    suspend fun deleteAll()
}