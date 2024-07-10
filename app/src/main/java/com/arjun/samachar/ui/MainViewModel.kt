package com.arjun.samachar.ui

import androidx.lifecycle.ViewModel
import com.arjun.samachar.data.model.Country
import com.arjun.samachar.data.remote.model.HeadlinesParams
import com.arjun.samachar.utils.AppConstants.DEFAULT_LANGUAGE_CODE
import com.arjun.samachar.utils.AppConstants.DEFAULT_SOURCE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // Network Status
    private val _isNetworkConnected = MutableStateFlow(false)

    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected

    fun updateNetworkStatus(isNetworkConnected: Boolean) {
        _isNetworkConnected.update { isNetworkConnected }
    }

    // Headline request params
    private val _headlinesParams = MutableStateFlow(HeadlinesParams())

    val headlinesParams: StateFlow<HeadlinesParams> = _headlinesParams

    fun updateSelectedCountry(country: Country) {
        _headlinesParams.update { it.copy(selectedCountry = country) }
    }

    fun updateSelectedLanguage(languageCode: String) {
        _headlinesParams.update { it.copy(selectedLanguageCode = languageCode) }
    }

    fun clearSelectedLanguage() {
        _headlinesParams.update { it.copy(selectedLanguageCode = DEFAULT_LANGUAGE_CODE) }
    }

    fun updateSelectedSource(source: String) {
        _headlinesParams.update { it.copy(selectedSourceId = source) }
    }

    fun clearSelectedSource() {
        _headlinesParams.update { it.copy(selectedSourceId = DEFAULT_SOURCE) }
    }

}