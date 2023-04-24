package cz.cvut.fit.biand.homework2.features.characters.data.api

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRemoteDataSource
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApiDescription {

    @GET("character")
    suspend fun getCharacters(): CharactersResponse

    @GET("character/")
    suspend fun getCharactersByName(@Query("name") name: String): CharactersResponse
}

@Serializable
data class CharactersResponse(val results: List<ApiCharacter>)

class CharacterRetrofitDataSource(
    private val apiDescription: CharacterApiDescription
    ) : CharacterRemoteDataSource {

    override suspend fun getCharacters(): List<DbCharacter> {
        return apiDescription.getCharacters().results.map { it.toDbCharacter() }
    }

    override suspend fun getCharactersByName(name: String): List<DbCharacter> {
        return apiDescription.getCharactersByName(name).results.map { it.toDbCharacter() }
    }

    private fun ApiCharacter.toDbCharacter(): DbCharacter {
        return DbCharacter(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = origin.name,
            location = location.name,
            imageUrl = image
        )
    }

}