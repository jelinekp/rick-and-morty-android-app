package cz.cvut.fit.biand.homework2.navigation

sealed class Screen(val route: String) {
    object ListScreen : Screen("list")
    object DetailScreen : Screen("detail")
    object SearchScreen : Screen("search")
}
