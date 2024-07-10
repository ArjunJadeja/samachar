package com.arjun.samachar.ui.base

import com.arjun.samachar.data.model.Country
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.data.model.Language
import com.arjun.samachar.data.model.Source

typealias ClickHandler = () -> Unit
typealias RetryHandler = () -> Unit
typealias DismissHandler = () -> Unit
typealias SearchHandler = (String) -> Unit
typealias UrlHandler = (String) -> Unit
typealias HeadlineHandler = (Headline) -> Unit
typealias SourceHandler = (Source) -> Unit
typealias LanguageHandler = (Language) -> Unit
typealias CountryHandler = (Country) -> Unit
