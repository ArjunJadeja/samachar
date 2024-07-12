package com.arjun.samachar.ui.headlines

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arjun.samachar.R
import com.arjun.samachar.data.model.HeadlineContract
import com.arjun.samachar.ui.base.MaxFillNoDataFound
import com.arjun.samachar.ui.base.ClickHandler
import com.arjun.samachar.ui.base.HeadlineHandler
import com.arjun.samachar.ui.base.MaxFillProgressLoading
import com.arjun.samachar.ui.base.RetryHandler
import com.arjun.samachar.ui.base.ShowErrorDialog
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.utils.StringsHelper.BOOKMARKED_NEWS
import com.arjun.samachar.utils.StringsHelper.DIALOG_ERROR_HEADER
import com.arjun.samachar.utils.StringsHelper.DIALOG_NETWORK_ERROR
import com.arjun.samachar.utils.StringsHelper.HEADLINE_IMAGE

@Composable
fun <T> LoadHeadlines(
    headlinesState: UiState<List<T>>,
    isNetworkConnected: Boolean,
    bookmarkIcon: Painter,
    onHeadlineClicked: HeadlineHandler<T>,
    onBookmarkClicked: HeadlineHandler<T>,
    onRetryClicked: RetryHandler,
    listState: LazyListState = rememberLazyListState()
) where T : HeadlineContract {

    when (headlinesState) {

        is UiState.Success -> {
            if (headlinesState.data.isNotEmpty()) {
                HeadlineList(
                    headlines = headlinesState.data,
                    bookmarkIcon = bookmarkIcon,
                    onHeadlineClicked = onHeadlineClicked,
                    onBookmarkClicked = onBookmarkClicked,
                    listState = listState
                )
            } else {
                MaxFillNoDataFound()
            }
        }

        is UiState.Loading -> {
            HandleHeadlineLoading(isNetworkConnected = isNetworkConnected)
        }

        is UiState.Error -> {
            ShowErrorDialog(
                header = DIALOG_ERROR_HEADER,
                message = if (isNetworkConnected) {
                    headlinesState.message
                } else {
                    DIALOG_NETWORK_ERROR
                }
            ) { onRetryClicked() }
        }
    }
}

@Composable
fun <T> HeadlineList(
    headlines: List<T>,
    bookmarkIcon: Painter,
    onHeadlineClicked: HeadlineHandler<T>,
    onBookmarkClicked: HeadlineHandler<T>,
    listState: LazyListState = rememberLazyListState()
) where T : HeadlineContract {

    LazyColumn(state = listState) {

        items(headlines,
            key = { headline -> "${headline.title}_${headline.url}_${headline.publishedAt}" }) { headline ->
            HeadlineItem(
                imageUrl = headline.imageUrl,
                bookmarkIcon = bookmarkIcon,
                author = headline.author,
                title = headline.title,
                publishedAt = headline.publishedAt,
                onClick = { onHeadlineClicked(headline) },
                onBookmarkClicked = { onBookmarkClicked(headline) }
            )
        }
    }
}

@Composable
fun <T> LoadPaginatedHeadlines(
    headlines: LazyPagingItems<T>,
    isNetworkConnected: Boolean,
    bookmarkIcon: Painter,
    onHeadlineClicked: HeadlineHandler<T>,
    onBookmarkClicked: HeadlineHandler<T>,
    onRetryClicked: RetryHandler,
    listState: LazyListState = rememberLazyListState()
) where T : HeadlineContract {

    PaginatedHeadlineList(
        headlines = headlines,
        bookmarkIcon = bookmarkIcon,
        onHeadlineClicked = onHeadlineClicked,
        onBookmarkClicked = onBookmarkClicked,
        listState = listState
    )

    headlines.apply {

        when (loadState.refresh) { // Initial loading
            is LoadState.Loading -> {
                HandleHeadlineLoading(isNetworkConnected = isNetworkConnected)
            }

            is LoadState.NotLoading -> {
                if (headlines.itemCount == 0) {
                    HandleHeadlineLoading(isNetworkConnected = isNetworkConnected)
                }
            }

            is LoadState.Error -> {
                val error = headlines.loadState.refresh as LoadState.Error
                Box(modifier = Modifier.fillMaxSize()) {
                    ShowErrorDialog(
                        header = DIALOG_ERROR_HEADER, message = if (isNetworkConnected) {
                            error.error.localizedMessage!!
                        } else {
                            DIALOG_NETWORK_ERROR
                        }
                    ) { onRetryClicked() }
                }
            }
        }

        when (loadState.append) { // Subsequent loading
            is LoadState.Loading -> {}

            is LoadState.NotLoading -> {}

            is LoadState.Error -> {}
        }
    }
}

@Composable
fun <T> PaginatedHeadlineList(
    headlines: LazyPagingItems<T>,
    bookmarkIcon: Painter,
    onHeadlineClicked: HeadlineHandler<T>,
    onBookmarkClicked: HeadlineHandler<T>,
    listState: LazyListState = rememberLazyListState()
) where T : HeadlineContract {
    LazyColumn(state = listState) {
        items(headlines.itemCount,
            key = { index -> "${headlines[index]?.source}_${headlines[index]?.url}_${headlines[index]?.publishedAt}" }) { index ->
            val headline = headlines[index]!!
            HeadlineItem(
                imageUrl = headline.imageUrl,
                bookmarkIcon = bookmarkIcon,
                author = headline.author,
                title = headline.title,
                publishedAt = headline.publishedAt,
                onClick = { onHeadlineClicked(headline) },
                onBookmarkClicked = { onBookmarkClicked(headline) }
            )
        }
    }
}

@Composable
fun HeadlineItem(
    imageUrl: String,
    bookmarkIcon: Painter,
    author: String,
    title: String,
    publishedAt: String,
    onClick: ClickHandler,
    onBookmarkClicked: ClickHandler
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        HeadlineBannerImage(
            imageUrl = imageUrl,
            bookmarkIcon = bookmarkIcon,
            onBookmarkClicked = onBookmarkClicked
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthorText(text = author)

        Spacer(modifier = Modifier.height(4.dp))

        TitleText(text = title)

        Spacer(modifier = Modifier.height(8.dp))

        PublishedAtText(text = publishedAt)

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp)
    }
}

@Composable
fun HandleHeadlineLoading(isNetworkConnected: Boolean) {
    if (isNetworkConnected) {
        MaxFillProgressLoading()
    } else {
        MaxFillNoDataFound()
    }
}

@Composable
private fun HeadlineBannerImage(
    imageUrl: String,
    bookmarkIcon: Painter,
    onBookmarkClicked: ClickHandler
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .placeholder(R.color.grey)
                .error(R.color.grey)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = HEADLINE_IMAGE,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .clickable { onBookmarkClicked() }
                .padding(4.dp)
        ) {
            Icon(
                painter = bookmarkIcon,
                contentDescription = BOOKMARKED_NEWS,
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun AuthorText(text: String) {
    if (text.isNotEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = text
        )
    }
}

@Composable
private fun TitleText(text: String) {
    if (text.isNotEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = text,
            fontSize = 20.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PublishedAtText(text: String) {
    if (text.isNotEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlineItemPreview() {
    HeadlineItem(
        imageUrl = "",
        bookmarkIcon = painterResource(id = R.drawable.add),
        author = "JEFFREY SCHAEFFER, JOHN LEICESTER",
        title = "Dow futures are little changed after blue-chip average touches 40,000 for first time: Live updates - CNBC",
        publishedAt = "2024-05-17T10:03:00Z",
        onClick = {},
        onBookmarkClicked = {})
}