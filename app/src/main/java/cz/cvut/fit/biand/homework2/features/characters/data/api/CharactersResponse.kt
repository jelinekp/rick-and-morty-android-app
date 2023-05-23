package cz.cvut.fit.biand.homework2.features.characters.data.api

import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(val results: List<ApiCharacter>)
