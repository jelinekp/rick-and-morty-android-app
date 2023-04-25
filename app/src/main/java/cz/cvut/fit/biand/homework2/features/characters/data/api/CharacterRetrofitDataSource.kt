package cz.cvut.fit.biand.homework2.features.characters.data.api

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRemoteDataSource
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter

class CharacterRetrofitDataSource(
    private val apiDescription: CharacterApiDescription
    ) : CharacterRemoteDataSource {

    override suspend fun getCharacters(): List<ApiCharacter> {
        return apiDescription.getCharacters().results // .map { it.toDbCharacter() }
    }

    override suspend fun getCharactersByName(name: String): List<ApiCharacter> {
        return apiDescription.getCharactersByName(name).results // .map { it.toDbCharacter() }
    }

}