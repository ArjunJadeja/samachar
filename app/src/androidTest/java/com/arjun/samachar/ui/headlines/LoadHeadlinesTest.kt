package com.arjun.samachar.ui.headlines

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.arjun.samachar.R
import com.arjun.samachar.data.remote.model.Headline
import com.arjun.samachar.data.remote.model.Source
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.utils.StringsHelper.DIALOG_NETWORK_ERROR
import com.arjun.samachar.utils.StringsHelper.LOADING
import com.arjun.samachar.utils.StringsHelper.NO_DATA_FOUND
import com.arjun.samachar.utils.StringsHelper.RETRY
import org.junit.Rule
import org.junit.Test

class LoadHeadlinesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadHeadlines_forUiStateLoadingAndNetworkConnected_showProgressLoading() {

        val networkConnected = true

        composeTestRule.setContent {
            LoadHeadlines(
                headlinesState = UiState.Loading as UiState<List<Headline>>,
                isNetworkConnected = networkConnected,
                bookmarkIcon = painterResource(id = R.drawable.add),
                onHeadlineClicked = {},
                onBookmarkClicked = {},
                onRetryClicked = { }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(LOADING)
            .assertExists()

    }

    @Test
    fun loadHeadlines_forUiStateLoadingAndNetworkNotConnected_showProgressLoading() {

        val networkConnected = false

        composeTestRule.setContent {
            LoadHeadlines(
                headlinesState = UiState.Loading as UiState<List<Headline>>,
                isNetworkConnected = networkConnected,
                bookmarkIcon = painterResource(id = R.drawable.add),
                onHeadlineClicked = {},
                onBookmarkClicked = {},
                onRetryClicked = { }
            )
        }

        composeTestRule
            .onNodeWithText(NO_DATA_FOUND)
            .assertExists()

    }

    @Test
    fun loadHeadlines_forUiStateSuccess_showHeadlineList() {

        composeTestRule.setContent {
            LoadHeadlines(
                headlinesState = UiState.Success(dummyHeadlineList),
                isNetworkConnected = true,
                bookmarkIcon = painterResource(id = R.drawable.add),
                onHeadlineClicked = {},
                onBookmarkClicked = {},
                onRetryClicked = { }
            )
        }

        composeTestRule
            .onNodeWithText(dummyHeadlineList[0].title, substring = true)
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(hasText(dummyHeadlineList[5].title, substring = true))

        composeTestRule
            .onNodeWithText(dummyHeadlineList[5].title, substring = true)
            .assertExists()
            .assertHasClickAction()

    }

    @Test
    fun loadHeadlines_forUiStateError_showErrorDialog() {

        composeTestRule.setContent {
            LoadHeadlines(
                headlinesState = UiState.Error(DIALOG_NETWORK_ERROR) as UiState<List<Headline>>,
                isNetworkConnected = true,
                bookmarkIcon = painterResource(id = R.drawable.add),
                onHeadlineClicked = {},
                onBookmarkClicked = {},
                onRetryClicked = { }
            )
        }

        composeTestRule
            .onNodeWithText(DIALOG_NETWORK_ERROR)
            .assertExists()

        composeTestRule
            .onNodeWithText(RETRY)
            .assertExists()

    }

}

private val dummyHeadlineList = listOf(
    Headline(
        title = "title1",
        description = "description1",
        url = "url1",
        imageUrl = "imageUrl1",
        source = Source(sourceId = "sourceId1", sourceName = "sourceName1")
    ),
    Headline(
        title = "title2",
        description = "description2",
        url = "url2",
        imageUrl = "imageUrl2",
        source = Source(sourceId = "sourceId2", sourceName = "sourceName2")
    ),
    Headline(
        title = "title3",
        description = "description3",
        url = "url3",
        imageUrl = "imageUrl3",
        source = Source(sourceId = "sourceId3", sourceName = "sourceName3")
    ),
    Headline(
        title = "title4",
        description = "description4",
        url = "url4",
        imageUrl = "imageUrl4",
        source = Source(sourceId = "sourceId4", sourceName = "sourceName4")
    ),
    Headline(
        title = "title5",
        description = "description5",
        url = "url5",
        imageUrl = "imageUrl5",
        source = Source(sourceId = "sourceId5", sourceName = "sourceName5")
    ),
    Headline(
        title = "title6",
        description = "description6",
        url = "url6",
        imageUrl = "imageUrl6",
        source = Source(sourceId = "sourceId6", sourceName = "sourceName6")
    )
)