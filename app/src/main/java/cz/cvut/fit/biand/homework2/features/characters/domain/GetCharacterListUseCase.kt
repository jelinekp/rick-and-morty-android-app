package cz.cvut.fit.biand.homework2.features.characters.domain

// Not implemented yet
/*class GetCharacterListUseCase(
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
}*/
