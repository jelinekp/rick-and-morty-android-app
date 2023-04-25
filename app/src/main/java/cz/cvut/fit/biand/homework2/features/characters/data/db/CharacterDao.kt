package cz.cvut.fit.biand.homework2.features.characters.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters") //  ORDER BY name ASC
    fun getAllCharacters(): Flow<List<DbCharacter>>

    @Query("SELECT * FROM characters WHERE is_favorite = 1")
    fun getFavoriteCharacters(): Flow<List<DbCharacter>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacter(id: String): Flow<DbCharacter?>

    @Insert
    suspend fun insert(dbCharacters: List<DbCharacter>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dbCharacter: DbCharacter)

    @Query("UPDATE characters SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Delete
    suspend fun delete(dbCharacter: DbCharacter)

    @Query("DELETE FROM characters")
    suspend fun deleteAll()

    @Query("SELECT is_favorite FROM characters WHERE id = :id")
    suspend fun isCharacterFavorite(id: String): Boolean
}
