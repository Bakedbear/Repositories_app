package com.codingtroops.examplepackage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.codingtroops.examplepackage.ui.theme.RepositoriesAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepositoriesAppTheme {
                val viewModel: RepositoriesViewModel = viewModel()
                val reposFlow = viewModel.repositories
                val lazyRepoItems: LazyPagingItems<Repository> =
                    reposFlow.collectAsLazyPagingItems()
                val timerText = viewModel.timerState.value

                RepositoriesScreen(lazyRepoItems, timerText,
                getTimer = {viewModel.timer},
                    onPauseTimer = {viewModel.timer.stop()}
                )
            }
        }
    }
}