package cz.cvut.fit.biand.homework2.features.characters.data.db

import androidx.room.*

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters") //  ORDER BY name ASC
    suspend fun getAllCharacters(): List<DbCharacter>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacter(id: Int): DbCharacter?

    @Insert
    suspend fun insert(dbCharacters: List<DbCharacter>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dbCharacter: DbCharacter)

    @Delete
    suspend fun delete(dbCharacter: DbCharacter)

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}
