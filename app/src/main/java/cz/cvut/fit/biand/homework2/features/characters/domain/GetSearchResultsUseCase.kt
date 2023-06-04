package cz.cvut.fit.biand.homework2.features.characters.domain

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterSearchResult
import kotlinx.coroutines.flow.first


class GetSearchResultsUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(query: String) : CharacterSearchResult {
        return if (query.isBlank()) {
            val allCharacters = characterRepository.getCharacters()
            CharacterSearchResult(
                allCharacters.characters.first(),
                allCharacters.isSuccess
            )
        }
        else {
            val searchedCharacters = characterRepository.getApiCharactersByName(name = query)
            CharacterSearchResult(
                searchedCharacters.characters,
                searchedCharacters.isSuccess,
            )
        }
    }
}
