package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.data.db.DbCharacter

data class CharactersResult(
    val characters: List<DbCharacter>,
    val isSuccess: Boolean
)
