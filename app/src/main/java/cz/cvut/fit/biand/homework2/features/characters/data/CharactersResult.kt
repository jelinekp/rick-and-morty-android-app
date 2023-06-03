package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.Flow

data class CharactersResult(
    val characters: Flow<List<Character>>,
    val isSuccess: Boolean
)
