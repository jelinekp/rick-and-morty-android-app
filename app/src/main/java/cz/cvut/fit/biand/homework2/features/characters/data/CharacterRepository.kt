package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter

class CharacterRepository(
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
) {

    suspend fun getCharacters(): CharactersResult {
        return try {
            val characters = characterRemoteDataSource.getCharacters()
            characterLocalDataSource.deleteAll()
            characterLocalDataSource.insert(characters)
            CharactersResult(characters, isSuccess = true)
        } catch (t: Throwable) {
            CharactersResult(characterLocalDataSource.getCharacters(), isSuccess = false)
        }
    }

    suspend fun getCharacter(id: Int): DbCharacter? {
        return characterLocalDataSource.getCharacter(id)
    }

    suspend fun getCharactersByName(name: String): CharactersResult {
        return try {
            val characters = characterRemoteDataSource.getCharactersByName(name)
            CharactersResult(characters, isSuccess = true)
        } catch (t: Throwable) {
            CharactersResult(emptyList(), isSuccess = false)
        }
    }

}