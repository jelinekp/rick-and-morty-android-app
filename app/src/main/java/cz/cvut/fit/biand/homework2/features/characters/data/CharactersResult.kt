package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter
import kotlinx.coroutines.flow.Flow

data class CharactersResult(
    val characters: Flow<List<DbCharacter>>,
    val isSuccess: Boolean
)
