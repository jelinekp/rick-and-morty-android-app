package cz.cvut.fit.biand.homework2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cz.cvut.fit.biand.homework2.core.presentation.theme.AppTheme
import cz.cvut.fit.biand.homework2.navigation.Navigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // Tahle část asi bude chtít předělat
                Navigation()
            }
        }
    }
}
