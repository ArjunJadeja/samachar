package com.arjun.samachar.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.arjun.samachar.R
import com.arjun.samachar.utils.StringsHelper.BACK_BUTTON
import com.arjun.samachar.utils.StringsHelper.LOADING
import com.arjun.samachar.utils.StringsHelper.NO_DATA_FOUND
import com.arjun.samachar.utils.StringsHelper.NO_INTERNET_CONNECTION
import com.arjun.samachar.utils.StringsHelper.RETRY
import com.arjun.samachar.utils.StringsHelper.VIEW_NEWS_OFFLINE

@Composable
fun PrimaryButton(modifier: Modifier, text: String, onClick: ClickHandler) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(size = 12.dp),
        onClick = onClick
    ) { Text(text = text, fontSize = 14.sp) }
}

@Composable
fun BackButton(onClick: ClickHandler) {
    IconButton(
        modifier = Modifier,
        drawablePainter = painterResource(id = R.drawable.arrow_back),
        contentDescription = BACK_BUTTON
    ) { onClick() }
}

@Composable
fun IconButton(
    modifier: Modifier,
    drawablePainter: Painter,
    contentDescription: String,
    onClick: ClickHandler
) {
    Icon(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        painter = drawablePainter,
        contentDescription = contentDescription
    )
}

@Composable
fun TextButton(modifier: Modifier, text: String, onClick: ClickHandler) {
    Text(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        text = text,
        fontSize = 24.sp
    )
}

@Composable
fun MaxFillProgressLoading() {
    Box(modifier = Modifier.fillMaxSize()) { ProgressLoading(modifier = Modifier.align(Alignment.Center)) }
}

@Composable
fun ProgressLoading(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier.semantics { contentDescription = LOADING },
        color = colorScheme.secondary,
        trackColor = colorScheme.surfaceVariant,
    )
}

@Composable
fun ShowErrorDialog(
    header: String,
    message: String,
    onRetryClick: RetryHandler,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AlertDialog(
            onDismissRequest = { /* No action to make it non-cancellable */ },
            title = { Text(text = header) },
            text = { Text(text = message) },
            confirmButton = { Button(onClick = onRetryClick) { Text(text = RETRY) } },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}

@Composable
fun NoNetworkStatusBar(onViewOfflineHeadlinesClick: ClickHandler) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(3.dp),
            text = NO_INTERNET_CONNECTION,
            fontSize = 14.sp
        )

        HorizontalDivider()

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
                .clickable { onViewOfflineHeadlinesClick() },
            text = VIEW_NEWS_OFFLINE,
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun MaxFillNoDataFound() {
    Box(modifier = Modifier.fillMaxSize()) { NoDataFound(modifier = Modifier.align(Alignment.Center)) }
}

@Composable
fun NoDataFound(modifier: Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_data_illustration),
            contentDescription = NO_DATA_FOUND
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = NO_DATA_FOUND)
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(modifier = Modifier, text = "Primary Button") {}
}

@Preview(showBackground = true)
@Composable
fun IconButtonPreview() {
    IconButton(
        modifier = Modifier,
        painterResource(id = R.drawable.arrow_back),
        contentDescription = ""
    ) {}
}

@Preview
@Composable
fun TextButtonPreview() {
    TextButton(modifier = Modifier, text = "Text Button") {}
}

@Preview(showBackground = true)
@Composable
fun MaxFillProgressLoadingPreview() {
    MaxFillProgressLoading()
}

@Preview(showBackground = true)
@Composable
fun ProgressLoadingPreview() {
    ProgressLoading(modifier = Modifier)
}

@Preview
@Composable
fun ApiRetryAlertPreview() {
    ShowErrorDialog(header = "Error title",
        message = "Error message",
        onRetryClick = { /* Retry logic */ })
}

@Preview(showBackground = true)
@Composable
fun NoNetworkStatusBarPreview() {
    NoNetworkStatusBar {}
}

@Preview(showBackground = true)
@Composable
fun MaxFillNoDataFoundPreview() {
    MaxFillNoDataFound()
}

@Preview(showBackground = true)
@Composable
fun NoDataFoundPreview() {
    NoDataFound(modifier = Modifier)
}
