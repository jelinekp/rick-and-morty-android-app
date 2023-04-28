package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CharacterRepository(
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
) {

    suspend fun getCharacters(): CharactersResult {
        return try {
            val characters = characterRemoteDataSource.getCharacters()
            val charactersWithFavorites = characters.map {
                // Log.d("Is character favorite", characterLocalDataSource.isCharacterFavorite(it.id.toString()).toString())
                it.toDbCharacter(characterLocalDataSource.isCharacterFavorite(it.id.toString()))
            }
            characterLocalDataSource.deleteAll()
            characterLocalDataSource.insert(charactersWithFavorites)
            CharactersResult(characterLocalDataSource.getCharacters(), isSuccess = true) // TODO: replace characterLocalDataSource.getCharacters() with a flow of "charactersWithFavorites"
        } catch (t: Throwable) {
            CharactersResult(characterLocalDataSource.getCharacters(), isSuccess = false)
        }
    }

    suspend fun getFavoriteCharacters(): Flow<List<DbCharacter>> {
        return characterLocalDataSource.getFavoriteCharacters()
    }

    suspend fun getCharacter(id: String): Flow<DbCharacter?> {
        return characterLocalDataSource.getCharacter(id)
    }

    suspend fun updateFavorite(id: String, isFavorite: Boolean) {
        return characterLocalDataSource.updateFavorite(id, isFavorite)
    }

    suspend fun getApiCharactersByName(name: String): CharactersResult {
        return try {
            val characters = characterRemoteDataSource.getCharactersByName(name)
            val localCharacters = characters.map { it.toDbCharacter(characterLocalDataSource.isCharacterFavorite(it.id.toString()))}
            CharactersResult(flowOf(localCharacters), isSuccess = true)
        } catch (t: Throwable) {
            CharactersResult(flowOf(emptyList()), isSuccess = false)
        }
    }

}