package cz.cvut.fit.biand.homework2.features.characters.di

import cz.cvut.fit.biand.homework2.core.data.db.RickAndMortyDatabase
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterLocalDataSource
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRemoteDataSource
import cz.cvut.fit.biand.homework2.features.characters.data.api.CharacterRetrofitDataSource
import cz.cvut.fit.biand.homework2.features.characters.data.db.CharacterRoomDataSource
import cz.cvut.fit.biand.homework2.features.characters.presentation.list.ListViewModel
import cz.cvut.fit.biand.homework2.features.characters.presentation.detail.CharacterDetailViewModel
import cz.cvut.fit.biand.homework2.features.characters.presentation.search.SearchViewModel
import cz.cvut.fit.biand.homework2.features.characters.data.CharacterRepository
import cz.cvut.fit.biand.homework2.features.characters.data.api.CharacterApiDescription
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import retrofit2.Retrofit
import cz.cvut.fit.biand.homework2.features.characters.domain.GetSearchResultsUseCase

val characterModule get() = module {
    single { get<Retrofit>().create(CharacterApiDescription::class.java) }
    factory<CharacterRemoteDataSource> { CharacterRetrofitDataSource(apiDescription = get()) }

    single { get<RickAndMortyDatabase>().characterDao() }
    factory<CharacterLocalDataSource> { CharacterRoomDataSource(characterDao = get()) }

    factoryOf(::CharacterRepository)
    factoryOf(::GetSearchResultsUseCase)

    viewModelOf(::ListViewModel)
    viewModelOf(::CharacterDetailViewModel)
    viewModelOf(::SearchViewModel)

}