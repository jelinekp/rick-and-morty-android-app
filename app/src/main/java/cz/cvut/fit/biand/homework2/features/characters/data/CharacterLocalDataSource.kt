package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {

    suspend fun getCharacters(): Flow<List<DbCharacter>>

    suspend fun getFavoriteCharacters(): Flow<List<DbCharacter>>

    suspend fun getCharacter(id: String): Flow<DbCharacter?>

    suspend fun isCharacterFavorite(id: String): Boolean

    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    suspend fun insert(characters: List<DbCharacter>)

    suspend fun deleteAll()
}