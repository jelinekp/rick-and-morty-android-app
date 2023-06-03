package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.model.Character
interface CharacterRemoteDataSource {

    suspend fun getCharacters(): List<Character>

    suspend fun getCharactersByName(name: String): List<Character>

    suspend fun getCharacterById(id: Int): Character?

}