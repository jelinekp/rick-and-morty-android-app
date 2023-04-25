package cz.cvut.fit.biand.homework2.features.characters.data.db

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterLocalDataSource
import kotlinx.coroutines.flow.Flow

class CharacterRoomDataSource(private val characterDao: CharacterDao) : CharacterLocalDataSource {

    override suspend fun getCharacters(): Flow<List<DbCharacter>> {
        return characterDao.getAllCharacters()
    }

    override suspend fun getFavoriteCharacters(): Flow<List<DbCharacter>> {
        return characterDao.getFavoriteCharacters()
    }

    override suspend fun getCharacter(id: String): Flow<DbCharacter?> {
        return characterDao.getCharacter(id)
    }

    override suspend fun isCharacterFavorite(id: String): Boolean {
        return characterDao.isCharacterFavorite(id)
    }

    override suspend fun updateFavorite(id: String, isFavorite: Boolean) {
        return characterDao.updateFavorite(id, isFavorite)
    }

    override suspend fun insert(characters: List<DbCharacter>) {
        return characterDao.insert(characters)
    }

    override suspend fun deleteAll() {
        return characterDao.deleteAll()
    }

}