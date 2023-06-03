package cz.cvut.fit.biand.homework2.features.characters.domain

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.CharactersResult
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow


class GetSearchResultsUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterName: String) : CharactersResult {
        // TODO: combine repository flow and api result
        val charactersByName = characterRepository.getApiCharactersByName(name = characterName)
        return CharactersResult(
            combine(
                charactersByName.characters,
                characterRepository.getFavoriteCharacters()
            ) { apiCharacters, storedCharacters ->
                val apiCharactersWithFavorites : List<Character> = apiCharacters.map { character ->
                    character.copy(isFavorite = storedCharacters.contains(character))
                }
                apiCharactersWithFavorites
            },
            charactersByName.isSuccess
        )
    }
}