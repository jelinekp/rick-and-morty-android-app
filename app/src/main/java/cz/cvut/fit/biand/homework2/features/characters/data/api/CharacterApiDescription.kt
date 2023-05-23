package cz.cvut.fit.biand.homework2.features.characters.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApiDescription {

    /**
     * [Reference](https://rickandmortyapi.com/documentation/#get-all-characters)
     * Getting only the first page.
     */
    @GET("character")
    suspend fun getCharacters(): CharactersResponse

    /**
     * [Reference](https://rickandmortyapi.com/documentation/#filter-characters)
     */
    @GET("character/")
    suspend fun getCharactersByName(@Query("name") name: String): CharactersResponse

    /**
     * [Reference](https://rickandmortyapi.com/documentation/#get-a-single-character)
     */
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): ApiCharacter? // nullable? is it necessary?
}