package cz.cvut.fit.biand.homework2.features.characters.data.api

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import kotlinx.serialization.Serializable

@Serializable
data class ApiCharacter(
    val id: Int,
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

    fun toDbCharacter(isFavorite: Boolean): DbCharacter {
        return DbCharacter(
            id = id.toString(),
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = origin.name,
            location = location.name,
            imageUrl = image,
            isFavorite = isFavorite
        )
    }
}
