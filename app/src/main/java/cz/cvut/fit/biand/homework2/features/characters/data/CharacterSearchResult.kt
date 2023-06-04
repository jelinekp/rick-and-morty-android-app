package cz.cvut.fit.biand.homework2.features.characters.data

import cz.cvut.fit.biand.homework2.features.characters.model.Character

data class CharacterSearchResult(
    val characters: List<Character>,
    val isSuccess: Boolean,
    val isConnected: Boolean = true,
)