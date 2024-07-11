package com.arjun.samachar.ui.headlines.search

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.arjun.samachar.R
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.BackButton
import com.arjun.samachar.ui.base.ClickHandler
import com.arjun.samachar.ui.base.NoNetworkStatusBar
import com.arjun.samachar.ui.base.Route
import com.arjun.samachar.ui.base.SearchHandler
import com.arjun.samachar.ui.headlines.LoadPaginatedHeadlines
import com.arjun.samachar.utils.StringsHelper.SAVED_TO_BOOKMARK
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun SearchScreen(
    context: Context,
    navController: NavController,
    mainViewModel: MainViewModel,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onHeadlineClicked: (String) -> Unit
) {
    val networkConnectedState by mainViewModel.isNetworkConnected.collectAsState()

    val searchedHeadlinesState = searchViewModel.searchedHeadlines.collectAsLazyPagingItems()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) { searchViewModel.search(query = searchQuery) }

    Scaffold(
        topBar = {
            TopAppBar(
                onBackPressed = { navController.navigateUp() },
                onQueryChange = { searchQuery = it }
            )
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            if (!networkConnectedState) {
                NoNetworkStatusBar { navController.navigate(Route.OfflineScreen.name) }
            }

            if (searchQuery.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    LoadPaginatedHeadlines(
                        headlines = searchedHeadlinesState,
                        isNetworkConnected = networkConnectedState,
                        bookmarkIcon = painterResource(id = R.drawable.add),
                        onHeadlineClicked = { onHeadlineClicked(it.url) },
                        onBookmarkClicked = {
                            searchViewModel.bookmarkHeadline(it)
                            Toast.makeText(context, SAVED_TO_BOOKMARK, Toast.LENGTH_SHORT).show()
                        },
                        onRetryClicked = { searchViewModel.search(searchViewModel.queryText.value) }
                    )
                }
            } else {
                searchViewModel.clearHeadlines()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onBackPressed: ClickHandler,
    onQueryChange: SearchHandler
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton { onBackPressed() }
                SearchField { onQueryChange(it) }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@OptIn(FlowPreview::class)
@Composable
private fun SearchField(onQueryChange: SearchHandler) {

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        MutableStateFlow(searchText)
            .debounce(300)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collect { onQueryChange(it) }
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        value = searchText,
        onValueChange = { query -> searchText = query },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    .padding(12.dp),
            ) {
                if (searchText.isEmpty()) {
                    Text(
                        text = "search...",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}