package cz.cvut.fit.biand.homework2.features.characters.data.db

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterLocalDataSource
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRoomDataSource(private val characterDao: CharacterDao) : CharacterLocalDataSource {

    override fun getCharactersFlow(): Flow<List<Character>> {
        return characterDao.getAllCharactersFlow().map { dbCharacterList ->
            dbCharacterList.map { dbCharacter ->
                dbCharacter.toCharacter()
            }
        }
    }

    override fun getFavoriteCharactersFlow(): Flow<List<Character>> {
        return characterDao.getFavoriteCharactersFlow().map { dbCharacterList ->
            dbCharacterList.map { dbCharacter ->
                dbCharacter.toCharacter()
            }
        }
    }

    override fun getCharacterFlow(id: String): Flow<Character?> {
        return characterDao.getCharacterFlow(id).map { it?.toCharacter() }
    }
    
    override suspend fun getCharacterById(id: String): Character? {
        return characterDao.getCharacter(id)?.toCharacter()
    }
    
    override suspend fun isCharacterFavorite(id: String): Boolean {
        return characterDao.isCharacterFavorite(id)
    }

    override suspend fun updateFavorite(character: Character, isFavorite: Boolean) {
        if (!characterDao.isCharacterStored(character.id)) {
            characterDao.insert(listOf(character.toDbCharacter()))
        }
        return characterDao.updateFavorite(character.id, isFavorite)
    }

    override suspend fun insert(characters: List<Character>) {
        return characterDao.insert(characters.map { it.toDbCharacter() })
    }

    override suspend fun deleteAll() {
        return characterDao.deleteAll()
    }

}