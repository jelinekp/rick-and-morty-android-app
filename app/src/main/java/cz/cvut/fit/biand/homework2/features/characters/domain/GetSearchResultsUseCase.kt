package cz.cvut.fit.biand.homework2.features.characters.domain

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterSearchResult
import kotlinx.coroutines.flow.first


class GetSearchResultsUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(query: String) : CharacterSearchResult {
        val charactersByName = characterRepository.getApiCharactersByName(name = query)
        val resultCharacters =
        if (query.isBlank())
            characterRepository.getCharacters().characters.first()
        else
            charactersByName.characters

        return CharacterSearchResult(
            characters = resultCharacters,
            isSuccess = charactersByName.isSuccess,
        )
    }
}
