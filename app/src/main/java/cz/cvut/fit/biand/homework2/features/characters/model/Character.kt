package cz.cvut.fit.biand.homework2.features.characters.model

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter

/**
 * Not needed at this time, the DbCharacter is just fine
 */
data class Character(

    val id: String,

    val name: String,

    val status: String,

    val species: String,

    val type: String,

    val gender: String,

    val origin: String,

    val location: String,

    val imageUrl: String,

    var isFavorite: Boolean

) {
    fun toDbCharacter() : DbCharacter {
        return DbCharacter(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = origin,
            location = location,
            imageUrl = imageUrl,
            isFavorite = isFavorite
        )
    }

    companion object {
        val emptyCharacter = Character(
            "666",
            "Empty Character",
            "Not saved in local database",
            "",
            "",
            "",
            "",
            "",
            "",
            false
        )
    }
}