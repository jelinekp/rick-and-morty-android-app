package cz.cvut.fit.biand.homework2.features.characters.data.api

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRemoteDataSource
import cz.cvut.fit.biand.homework2.features.characters.model.Character

class CharacterRetrofitDataSource(
    private val apiDescription: CharacterApiDescription
) : CharacterRemoteDataSource {

    override suspend fun getCharacters(): List<Character> {
        return apiDescription.getCharacters().results.map { it.toCharacter() }
    }

    override suspend fun getCharactersByName(name: String): List<Character> {
        return apiDescription.getCharactersByName(name).results.map { it.toCharacter() }
    }

    override suspend fun getCharacterById(id: Int): Character? {
        return apiDescription.getCharacterById(id)?.toCharacter()
    }
}