package com.arjun.samachar.ui.filters.language

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arjun.samachar.data.model.Language
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.LanguageHandler
import com.arjun.samachar.ui.base.ClickHandler
import com.arjun.samachar.ui.base.DismissHandler
import com.arjun.samachar.ui.base.PrimaryButton
import com.arjun.samachar.ui.base.ProgressLoading
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.utils.StringsHelper.CHOOSE_LANGUAGE
import com.arjun.samachar.utils.StringsHelper.SELECT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagesBottomSheet(
    context: Context,
    languageViewModel: LanguageViewModel,
    mainViewModel: MainViewModel,
    selectedLanguageCode: String,
    onDismiss: DismissHandler
) {
    var selectedLanguage by remember { mutableStateOf(selectedLanguageCode) }

    val languagesState by languageViewModel.languageList.collectAsStateWithLifecycle()

    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        LoadLanguages(
            context = context,
            languagesState = languagesState,
            selectedLanguage = selectedLanguage,
            onLanguageSelected = { language ->
                selectedLanguage = language.code
                mainViewModel.apply {
                    clearSelectedLanguage()
                    updateSelectedLanguage(selectedLanguage)
                }
            },
            onDismiss = { onDismiss() }
        )
    }
}

@Composable
private fun LoadLanguages(
    context: Context,
    languagesState: UiState<List<Language>>,
    selectedLanguage: String,
    onLanguageSelected: LanguageHandler,
    onDismiss: DismissHandler
) {
    when (languagesState) {
        is UiState.Success -> {
            if (languagesState.data.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TitleText(modifier = Modifier.align(Alignment.CenterHorizontally))

                    Spacer(modifier = Modifier.height(8.dp))

                    LanguageGrid(
                        languages = languagesState.data,
                        selectedLanguageCode = selectedLanguage
                    ) { onLanguageSelected(it) }

                    SelectButton { onDismiss() }
                }
            }
        }

        UiState.Loading -> {
            Box(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
            ) { ProgressLoading(modifier = Modifier.align(Alignment.Center)) }
        }

        is UiState.Error -> {
            Toast.makeText(context, languagesState.message, Toast.LENGTH_SHORT).show()
            onDismiss()
        }
    }
}

@Composable
private fun LanguageGrid(
    languages: List<Language>,
    selectedLanguageCode: String,
    onLanguageSelected: LanguageHandler
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(languages, key = { language -> language.code }) { language ->
            LanguageItem(
                language = language,
                isSelected = language.code == selectedLanguageCode,
                onLanguageSelected = { onLanguageSelected(it) }
            )
        }
    }
}

@Composable
private fun LanguageItem(language: Language, isSelected: Boolean, onLanguageSelected: LanguageHandler) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary,
        shadowElevation = 1.dp,
        modifier = Modifier.clickable { onLanguageSelected(language) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Text(
                text = language.nativeName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TitleText(modifier: Modifier) {
    Text(
        text = CHOOSE_LANGUAGE,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
private fun SelectButton(onClick: ClickHandler) {
    PrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        text = SELECT,
    ) { onClick() }
}
