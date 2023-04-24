package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter

interface CharacterLocalDataSource {

    suspend fun getCharacters(): List<DbCharacter>

    suspend fun getCharacter(id: Int): DbCharacter?

    suspend fun insert(characters: List<DbCharacter>)

    suspend fun deleteAll()
}