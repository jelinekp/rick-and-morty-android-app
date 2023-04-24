package cz.cvut.fit.biand.homework2.features.characters.data.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiCharacter(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
) {

    @Serializable
    data class Origin(
        val name: String,
        val url: String,
    )

    @Serializable
    data class Location(
        val name: String,
        val url: String,
    )
}
