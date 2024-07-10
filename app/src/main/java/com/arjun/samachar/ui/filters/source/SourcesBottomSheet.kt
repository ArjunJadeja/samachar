package com.arjun.samachar.ui.filters.source

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arjun.samachar.data.remote.model.Source
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.DismissHandler
import com.arjun.samachar.ui.base.ProgressLoading
import com.arjun.samachar.ui.base.SourceHandler
import com.arjun.samachar.ui.base.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesBottomSheet(
    context: Context,
    sourcesViewModel: SourcesViewModel,
    mainViewModel: MainViewModel,
    countryCode: String,
    onDismiss: DismissHandler
) {
    LaunchedEffect(countryCode) {
        sourcesViewModel.getSources(countryCode)
    }

    val sourcesState by sourcesViewModel.sourceList.collectAsState()

    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        LoadSources(context = context, sourcesState = sourcesState, mainViewModel = mainViewModel) {
            onDismiss()
        }
    }
}

@Composable
private fun LoadSources(
    context: Context,
    sourcesState: UiState<List<Source>>,
    mainViewModel: MainViewModel,
    onDismiss: DismissHandler
) {
    when (sourcesState) {
        is UiState.Success -> {
            if (sourcesState.data.isNotEmpty()) {
                SourceList(sourceList = sourcesState.data) { source ->
                    mainViewModel.apply {
                        clearSelectedLanguage()
                        updateSelectedSource(source.sourceId)
                    }
                    onDismiss()
                }
            }
        }

        UiState.Loading -> {
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
            ) { ProgressLoading(modifier = Modifier.align(Alignment.Center)) }
        }

        is UiState.Error -> {
            Toast.makeText(context, sourcesState.message, Toast.LENGTH_SHORT).show()
            onDismiss()
        }
    }
}

@Composable
private fun SourceList(
    sourceList: List<Source>,
    onSourceSelected: SourceHandler
) {
    val updatedSourceList = listOf(Source()) + sourceList
    LazyColumn(modifier = Modifier.padding(bottom = 16.dp)) {
        items(updatedSourceList, key = { source -> source.sourceId }) { source ->
            SourceItem(source = source) { onSourceSelected(it) }
        }
    }
}

@Composable
private fun SourceItem(
    source: Source,
    onSourceSelected: SourceHandler
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onSourceSelected(source) },
        text = source.sourceName,
        textAlign = TextAlign.Center,
        fontSize = 18.sp
    )

    HorizontalDivider(thickness = 1.dp)
}
