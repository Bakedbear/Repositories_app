package com.codingtroops.examplepackage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed


@Composable
fun RepositoriesScreen(
    repos: LazyPagingItems<Repository>,
    timerText: String,
    getTimer: () -> CustomCountdown,
    onPauseTimer: () -> Unit
)
{
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        )
    ) {
        item {
            CountdownItem(
                timerText,
                getTimer,
                onPauseTimer
            )
        }
        itemsIndexed(repos){ index, repo ->
            if (repo != null){
                RepositoryItem(index, repo)
            }
        }
        val refreshLoadState = repos.loadState.refresh
        val appendLoadState = repos.loadState.append
        when {
            refreshLoadState is LoadState.Loading -> {
                item {
                    LoadingItem(
                        Modifier.fillParentMaxSize()
                    )
                }
            }
            refreshLoadState is LoadState.Error -> {
                val error = refreshLoadState.error
                item {
                    ErrorItem(
                        message = error.localizedMessage ?:""
                    ) { (repos.retry()) }
                }
            }
            appendLoadState is LoadState.Loading -> {
                item {
                    LoadingItem(
                        Modifier.fillMaxWidth()
                    )
                }
            }
            appendLoadState is LoadState.Error -> {
                val error = appendLoadState.error
                item{
                    ErrorItem(
                        message = error.localizedMessage ?:""
                    ) { (repos.retry()) }
                }
            }
        }

    }
}

@Composable
fun ErrorItem(message: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = message,
            maxLines = 2,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
            color = Color.Red
        )
        Button(
            onClick =  onClick ,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Try again")
        }
    }

}

@Composable
fun LoadingItem(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        
    }

}

@Composable
fun RepositoryItem(index: Int, item: Repository) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .padding(8.dp)
            .height(120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(8.dp))
            Column(modifier = Modifier.weight(0.75f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.h6)
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3)
            }
        }
    }
}
@Composable
private fun CountdownItem(
    timerText: String,
    getTimer: () -> CustomCountdown,
    onPauseTimer: () -> Unit
)
{
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    DisposableEffect(key1 = lifecycleOwner){
        lifecycle.addObserver(getTimer())
        onDispose {
            onPauseTimer()
            lifecycle.removeObserver(getTimer())

        }
    }
    Text(timerText)

}