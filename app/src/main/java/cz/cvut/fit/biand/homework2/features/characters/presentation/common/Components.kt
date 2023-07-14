package cz.cvut.fit.biand.homework2.features.characters.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.cvut.fit.biand.homework2.R

/**
 * Top left back icon and its action handling
 */
@Composable
fun BackIcon(
    backIconAction: () -> Unit
) {
    IconButton(onClick = backIconAction) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = stringResource(R.string.backIcon),
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * Common for Characters and SearchScreen
 */
@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

/**
 * Common for Characters, Favorites and SearchScreen
 */
@Composable
fun FavoriteIconIndicator(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isFavorite) {
        androidx.compose.material.Icon(
            painter = painterResource(id = R.drawable.ic_favorites_filled),
            contentDescription = stringResource(id = R.string.characterIsInFavorites),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .padding(start = 5.dp, top = 5.dp)
                .size(14.dp)
        )
    }
}