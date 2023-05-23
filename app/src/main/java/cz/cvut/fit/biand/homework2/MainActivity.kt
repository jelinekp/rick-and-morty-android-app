package cz.cvut.fit.biand.homework2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import cz.cvut.fit.biand.homework2.core.presentation.theme.AppTheme
import cz.cvut.fit.biand.homework2.navigation.Navigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val systemUiController = rememberSystemUiController()
                val appBackgroundColor = MaterialTheme.colorScheme.background
                val appNavBarColor = MaterialTheme.colorScheme.primaryContainer
                val useDarkIcons = !isSystemInDarkTheme()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = appBackgroundColor,
                        darkIcons = useDarkIcons
                    )
                    systemUiController.setNavigationBarColor(
                        color = appNavBarColor,
                        darkIcons = useDarkIcons
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = appBackgroundColor
                ) {
                    Navigation()
                }
            }
        }
    }
}
