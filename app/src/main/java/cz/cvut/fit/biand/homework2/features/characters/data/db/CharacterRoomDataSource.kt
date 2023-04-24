package cz.cvut.fit.biand.homework2.features.characters.data.db

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterLocalDataSource

class CharacterRoomDataSource(private val characterDao: CharacterDao) : CharacterLocalDataSource {

    override suspend fun getCharacters(): List<DbCharacter> {
        return characterDao.getAllCharacters()
    }

    override suspend fun getCharacter(id: Int): DbCharacter? {
        return characterDao.getCharacter(id)
    }

    override suspend fun insert(characters: List<DbCharacter>) {
        return characterDao.insert(characters)
    }

    override suspend fun deleteAll() {
        return characterDao.deleteAll()
    }

}