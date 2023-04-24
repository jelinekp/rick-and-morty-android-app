package cz.cvut.fit.biand.homework2.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import cz.cvut.fit.biand.homework2.features.characters.data.db.CharacterDao

@Database(entities = [DbCharacter::class], version = 3)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {

        fun instance(appContext: Context): RickAndMortyDatabase {
            return Room.databaseBuilder(
                context = appContext,
                klass = RickAndMortyDatabase::class.java,
                name = "rick_and_morty_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}