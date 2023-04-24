package cz.cvut.fit.biand.homework2.navigation

sealed class Screens(val route: String) {
    object ListScreen : Screens("list")
    class DetailScreen(characterId: String) : Screens("characters/$characterId") {

        companion object {
            const val ID = "id"
        }
    }
    object SearchScreen : Screens("search")
}
