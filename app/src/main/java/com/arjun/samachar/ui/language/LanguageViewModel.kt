package com.arjun.samachar.ui.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.model.Language
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LanguageViewModel(private val repository: MainRepository) : ViewModel() {

    private val _languageList = MutableStateFlow<UiState<List<Language>>>(UiState.Loading)

    val languageList: StateFlow<UiState<List<Language>>> = _languageList

    init {
        getAllLanguages()
    }

    private fun getAllLanguages() {
        viewModelScope.launch {
            repository.getAllLanguages()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _languageList.value = UiState.Error(e.message.toString())
                }
                .collect {
                    _languageList.value = UiState.Success(it)
                }
        }
    }

}