package cz.cvut.fit.biand.homework2.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fit.biand.homework2.R

sealed class Screens(val route: String) {
    class DetailScreen(characterId: String) : Screens("characters/$characterId") {

        companion object {
            const val ID = "id"
        }
    }
    object SearchScreen : Screens("search")
}

sealed class BottomBarScreen(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    object Characters : BottomBarScreen (
        route = "list",
        title = R.string.characters,
        icon = R.drawable.ic_characters
    )

    object Favorites : BottomBarScreen (
        route = "favorites",
        title = R.string.favorites,
        icon = R.drawable.ic_favorites_filled
    )
}