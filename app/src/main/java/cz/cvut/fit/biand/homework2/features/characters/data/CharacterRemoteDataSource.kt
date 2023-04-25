package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.api.ApiCharacter

interface CharacterRemoteDataSource {

    suspend fun getCharacters(): List<ApiCharacter>

    suspend fun getCharactersByName(name: String): List<ApiCharacter>

}