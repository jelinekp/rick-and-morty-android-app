package cz.cvut.fit.biand.homework2.data

import cz.cvut.fit.biand.homework2.R
import cz.cvut.fit.biand.homework2.model.Character

object CharactersDataSource {

    private val characters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "-",
            gender = "Male",
            origin = "Earth (C - 137)",
            location = "Citadel of Ricks",
            imageRes = R.drawable.avatar1,
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            type = "-",
            gender = "Male",
            origin = "Unknown",
            location = "Citadel of Ricks",
            imageRes = R.drawable.avatar2,
        ),
        Character(
            id = 3,
            name = "Summer Smith",
            status = "Alive",
            species = "Human",
            type = "-",
            gender = "Female",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            imageRes = R.drawable.avatar3,
        ),
        Character(
            id = 4,
            name = "Beth Smith",
            status = "Alive",
            species = "Human",
            type = "-",
            gender = "Female",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            imageRes = R.drawable.avatar4,
        ),
        Character(
            id = 5,
            name = "Jerry Smith",
            status = "Alive",
            species = "Human",
            type = "-",
            gender = "Male",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            imageRes = R.drawable.avatar5,
        ),
        Character(
            id = 6,
            name = "Abadango Cluster Princess",
            status = "Alive",
            species = "Alien",
            type = "-",
            gender = "Female",
            origin = "Abadango",
            location = "Abadango",
            imageRes = R.drawable.avatar6,
        ),
    )

    fun getAllCharacters(): List<Character> = characters

    fun getCharacter(id: Int) = characters.find { character -> id == character.id }
}
