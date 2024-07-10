package com.arjun.samachar.data.remote.model

import com.arjun.samachar.data.model.Country
import com.arjun.samachar.utils.AppConstants.DEFAULT_COUNTRY_CODE
import com.arjun.samachar.utils.AppConstants.DEFAULT_COUNTRY_FLAG
import com.arjun.samachar.utils.AppConstants.DEFAULT_COUNTRY_NAME
import com.arjun.samachar.utils.AppConstants.DEFAULT_LANGUAGE_CODE
import com.arjun.samachar.utils.AppConstants.DEFAULT_SOURCE

data class HeadlinesParams(
    val selectedCountry: Country = Country(
        code = DEFAULT_COUNTRY_CODE,
        name = DEFAULT_COUNTRY_NAME,
        flag = DEFAULT_COUNTRY_FLAG
    ),
    val selectedLanguageCode: String = DEFAULT_LANGUAGE_CODE,
    val selectedSourceId: String = DEFAULT_SOURCE
)