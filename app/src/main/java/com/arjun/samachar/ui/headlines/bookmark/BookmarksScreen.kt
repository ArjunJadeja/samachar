package com.arjun.samachar.ui.headlines.bookmark

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arjun.samachar.R
import com.arjun.samachar.ui.base.BackButton
import com.arjun.samachar.ui.base.ClickHandler
import com.arjun.samachar.ui.base.UrlHandler
import com.arjun.samachar.ui.headlines.LoadHeadlines
import com.arjun.samachar.utils.StringsHelper.BOOKMARKED_NEWS
import com.arjun.samachar.utils.StringsHelper.REMOVED_FROM_BOOKMARKS

@Composable
fun BookmarksScreen(
    context: Context,
    navController: NavController,
    bookmarksViewModel: BookmarksViewModel = hiltViewModel(),
    onHeadlineClicked: UrlHandler
) {
    val savedHeadlinesState by bookmarksViewModel.headlineList.collectAsState()

    Scaffold(
        topBar = { TopAppBar { navController.navigateUp() } }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                LoadHeadlines(
                    headlinesState = savedHeadlinesState,
                    isNetworkConnected = true,
                    bookmarkIcon = painterResource(id = R.drawable.delete),
                    onHeadlineClicked = { onHeadlineClicked(it.url) },
                    onBookmarkClicked = {
                        bookmarksViewModel.removeFromBookmarkedHeadlines(it)
                        Toast.makeText(context, REMOVED_FROM_BOOKMARKS, Toast.LENGTH_SHORT).show()
                    },
                    onRetryClicked = { /*Do nothing*/ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(onBackPressed: ClickHandler) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton { onBackPressed() }

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    text = BOOKMARKED_NEWS
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}