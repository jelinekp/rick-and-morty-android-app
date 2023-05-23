package cz.cvut.fit.biand.homework2.features.characters.domain

import androidx.annotation.DrawableRes

/**
 * Not needed at this time, the DbCharacter is just fine
 */
data class Character(

    val id: Int,

    val name: String,

    val status: String,

    val species: String,

    val type: String,

    val gender: String,

    val origin: String,

    val location: String,

    @DrawableRes
    val imageRes: Int,

    val isFavorite: Boolean

)