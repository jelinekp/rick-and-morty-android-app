package cz.cvut.fit.biand.homework2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cz.cvut.fit.biand.homework2.navigation.Navigation
import cz.cvut.fit.biand.homework2.presentation.DetailViewModel
import cz.cvut.fit.biand.homework2.presentation.ListViewModel
import cz.cvut.fit.biand.homework2.presentation.SearchViewModel
import cz.cvut.fit.biand.homework2.ui.theme.Homework2Theme

class MainActivity : ComponentActivity() {
    // Tahle část asi bude chtít předělat
    private val listViewModel: ListViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homework2Theme {
                // Tahle část asi bude chtít předělat
                Navigation(listViewModel, detailViewModel, searchViewModel)
            }
        }
    }
}
