package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter

interface CharacterRemoteDataSource {

    suspend fun getCharacters(): List<DbCharacter>

    suspend fun getCharactersByName(name: String): List<DbCharacter>

}