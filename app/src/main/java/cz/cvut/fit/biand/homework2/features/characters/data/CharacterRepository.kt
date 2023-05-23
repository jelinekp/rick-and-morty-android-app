package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.data.db.emptyCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

class CharacterRepository(
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
) {

    /**
     * Loading the characters primarily from Retrofit unless it fails
     *  - then local Room datastore is used.
     *  Downloading fresh data from Retrofit data source won't override favorites info
     */
    suspend fun getCharacters(): CharactersResult {
        return try {
            val characters = characterRemoteDataSource.getCharacters()
            val charactersWithFavorites = characters.map {
                // Log.d("Is character favorite", characterLocalDataSource.isCharacterFavorite(it.id.toString()).toString())
                it.toDbCharacter(
                    characterLocalDataSource.isCharacterFavorite(it.id.toString()) ?: false
                )
            }
            characterLocalDataSource.deleteAll()
            characterLocalDataSource.insert(charactersWithFavorites)
            CharactersResult(
                characterLocalDataSource.getCharacters(), // TODO: replace characterLocalDataSource.getCharacters() with a flow of "charactersWithFavorites"
                isSuccess = true
            )
        } catch (t: Throwable) {
            CharactersResult(characterLocalDataSource.getCharacters(), isSuccess = false)
        }
    }

    suspend fun getFavoriteCharacters(): Flow<List<DbCharacter>> {
        return characterLocalDataSource.getFavoriteCharacters()
    }

    /**
     * Loading the character from Retrofit, when it is not in local Room database
     */
    suspend fun getCharacter(id: String): Flow<DbCharacter?> {
        val localCharacter = characterLocalDataSource.getCharacter(id)
        return if (localCharacter.firstOrNull() != null) localCharacter
        else {
            val remoteCharacter =
                characterRemoteDataSource.getCharacterById(id.toInt())?.toDbCharacter(false)
            if (remoteCharacter != null)
                characterLocalDataSource.insert(listOf(remoteCharacter))
            else
                flowOf(emptyCharacter)
            characterLocalDataSource.getCharacter(id)
        }
    }

    suspend fun updateFavorite(id: String, isFavorite: Boolean) {
        return characterLocalDataSource.updateFavorite(id, isFavorite)
    }

    /**
     * Used by search feature, using [API endpoint](https://rickandmortyapi.com/documentation/#filter-characters)
     */
    suspend fun getApiCharactersByName(name: String): CharactersResult {
        return try {
            val characters = characterRemoteDataSource.getCharactersByName(name)
            val charactersWithFavorites = characters.map {
                it.toDbCharacter(
                    characterLocalDataSource.isCharacterFavorite(it.id.toString()) ?: false
                )
            }
            CharactersResult(flowOf(charactersWithFavorites), isSuccess = true)
        } catch (t: Throwable) {
            CharactersResult(flowOf(emptyList()), isSuccess = false)
        }
    }

}