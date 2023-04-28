package cz.cvut.fit.biand.homework2.features.characters.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class DbCharacter(

    @PrimaryKey val id: String,

    val name: String,

    val status: String,

    val species: String,

    val type: String,

    val gender: String,

    val origin: String,

    val location: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
)

val emptyCharacter = DbCharacter("666", "Empty Character", "Not saved in local database", "", "", "", "", "", "", false)
