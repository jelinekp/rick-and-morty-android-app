package cz.cvut.fit.biand.homework2.features.characters.data

import android.util.Log
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import cz.cvut.fit.biand.homework2.features.characters.model.Character

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
            val charactersWithFavorites = characters.map { character ->
                character.copy(isFavorite = characterLocalDataSource.isCharacterFavorite(character.id))
            }
            characterLocalDataSource.insert(charactersWithFavorites)
            CharactersResult(
                characterLocalDataSource.getCharacters(), // TODO: replace characterLocalDataSource.getCharacters() with a flow of "charactersWithFavorites"
                isSuccess = true
            )
        } catch (t: Throwable) {
            Log.d("ERROR", t.message.toString())
            CharactersResult(characterLocalDataSource.getCharacters(), isSuccess = false)
        }
    }

    fun getFavoriteCharacters(): Flow<List<Character>> {
        return characterLocalDataSource.getFavoriteCharacters()
    }

    /**
     * Loading the character from Retrofit, when it is not in local Room database
     */
    suspend fun getCharacter(id: String): Flow<Character?> {
        val localCharacter = characterLocalDataSource.getCharacter(id)
        return if (localCharacter.firstOrNull() != null) localCharacter
        else {
            val remoteCharacter =
                characterRemoteDataSource.getCharacterById(id.toInt())?.copy(isFavorite = false)
            if (remoteCharacter != null)
                characterLocalDataSource.insert(listOf(remoteCharacter))
            else
                flowOf(Character.emptyCharacter)
            characterLocalDataSource.getCharacter(id)
        }
    }

    suspend fun updateFavorite(character: Character, isFavorite: Boolean) {
        return characterLocalDataSource.updateFavorite(character, isFavorite)
    }

    /**
     * Used by search feature, using [API endpoint](https://rickandmortyapi.com/documentation/#filter-characters)
     */
    suspend fun getApiCharactersByName(name: String): CharactersResult {
        return try {
            val characters = characterRemoteDataSource.getCharactersByName(name)
            val charactersWithFavorites = characters.map { character ->
                character.copy(isFavorite = characterLocalDataSource.isCharacterFavorite(character.id))
            }
            CharactersResult(flowOf(charactersWithFavorites), isSuccess = true)
        } catch (t: Throwable) {
            CharactersResult(flowOf(emptyList()), isSuccess = false)
        }
    }

}