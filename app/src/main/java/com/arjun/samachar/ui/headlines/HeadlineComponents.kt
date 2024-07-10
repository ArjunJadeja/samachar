package com.arjun.samachar.ui.headlines

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arjun.samachar.R
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.ui.base.HeadlineHandler
import com.arjun.samachar.ui.base.MaxFillNoDataFound
import com.arjun.samachar.ui.base.MaxFillProgressLoading
import com.arjun.samachar.ui.base.ClickHandler
import com.arjun.samachar.ui.base.RetryHandler
import com.arjun.samachar.ui.base.ShowErrorDialog
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.utils.StringsHelper.DIALOG_ERROR_HEADER
import com.arjun.samachar.utils.StringsHelper.DIALOG_NETWORK_ERROR
import com.arjun.samachar.utils.StringsHelper.HEADLINE_IMAGE

@Composable
fun LoadHeadlines(
    headlinesState: UiState<List<Headline>>,
    isNetworkConnected: Boolean,
    onHeadlineClicked: HeadlineHandler,
    onRetryClicked: RetryHandler,
    listState: LazyListState = rememberLazyListState()
) {
    when (headlinesState) {
        is UiState.Success -> {
            if (headlinesState.data.isNotEmpty()) {
                HeadlineList(
                    headlines = headlinesState.data,
                    onHeadlineClicked = onHeadlineClicked,
                    listState = listState
                )
            } else {
                MaxFillNoDataFound()
            }
        }

        is UiState.Loading -> {
            MaxFillProgressLoading()
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
fun HeadlineList(
    headlines: List<Headline>,
    onHeadlineClicked: HeadlineHandler,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(state = listState) {
        items(headlines,
            key = { headline -> "${headline.title}_${headline.url}_${headline.publishedAt}" }) { headline ->
            HeadlineItem(
                imageUrl = headline.imageUrl,
                author = headline.author,
                title = headline.title,
                publishedAt = headline.publishedAt
            ) { onHeadlineClicked(headline) }
        }
    }
}

@Composable
fun HeadlineItem(
    imageUrl: String, author: String, title: String, publishedAt: String, onClick: ClickHandler
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        HeadlineBannerImage(imageUrl = imageUrl)

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
private fun HeadlineBannerImage(imageUrl: String) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
            .placeholder(R.color.grey).error(R.color.grey).crossfade(true).build(),
        contentScale = ContentScale.FillBounds,
        contentDescription = HEADLINE_IMAGE
    )
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
    HeadlineItem(imageUrl = "",
        author = "JEFFREY SCHAEFFER, JOHN LEICESTER",
        title = "Dow futures are little changed after blue-chip average touches 40,000 for first time: Live updates - CNBC",
        publishedAt = "2024-05-17T10:03:00Z",
        onClick = {})
}