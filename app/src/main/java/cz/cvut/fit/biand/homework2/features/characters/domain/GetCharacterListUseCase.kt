package cz.cvut.fit.biand.homework2.features.characters.domain

import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.CharactersResult
import cz.cvut.fit.biand.homework2.features.characters.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


class GetCharacterListUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(filter: CharactersFilter = CharactersFilter.ALL) : CharactersResult {
        val repositoryResult = characterRepository.getCharacters()
        return CharactersResult(
            if (filter == CharactersFilter.FAVORITES)
                repositoryResult.characters.map { characterList ->
                    characterList.filter { character ->
                        character.isFavorite
                    }
                }
            else
                repositoryResult.characters,
            repositoryResult.isSuccess
        )
    }
}

enum class CharactersFilter {
    ALL,
    FAVORITES
}
