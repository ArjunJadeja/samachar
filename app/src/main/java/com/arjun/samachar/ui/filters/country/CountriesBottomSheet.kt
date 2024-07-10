package com.arjun.samachar.ui.filters.country

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arjun.samachar.data.model.Country
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.CountryHandler
import com.arjun.samachar.ui.base.DismissHandler
import com.arjun.samachar.ui.base.ProgressLoading
import com.arjun.samachar.ui.base.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesBottomSheet(
    context: Context,
    countriesViewModel: CountriesViewModel,
    mainViewModel: MainViewModel,
    onDismiss: DismissHandler
) {
    val countriesState by countriesViewModel.countryList.collectAsState()

    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        LoadCountries(
            context = context,
            countriesState = countriesState,
            mainViewModel = mainViewModel
        ) { onDismiss() }
    }
}

@Composable
private fun LoadCountries(
    context: Context,
    countriesState: UiState<List<Country>>,
    mainViewModel: MainViewModel,
    onDismiss: DismissHandler
) {
    when (countriesState) {
        is UiState.Success -> {
            if (countriesState.data.isNotEmpty()) {
                CountryList(countries = countriesState.data) { country ->
                    mainViewModel.apply {
                        clearSelectedLanguage()
                        clearSelectedSource()
                        updateSelectedCountry(country)
                    }
                    onDismiss()
                }
            }
        }

        is UiState.Loading -> {
            Box(modifier = Modifier
                .height(140.dp)
                .fillMaxWidth()) {
                ProgressLoading(modifier = Modifier.align(Alignment.Center))
            }
        }

        is UiState.Error -> {
            Toast.makeText(context, countriesState.message, Toast.LENGTH_SHORT).show()
            onDismiss()
        }
    }
}

@Composable
private fun CountryList(
    countries: List<Country>,
    onCountrySelected: CountryHandler
) {
    LazyColumn {
        items(countries, key = { country -> country.code }) { country ->
            CountryItem(country = country) { onCountrySelected(it) }
        }
    }
}

@Composable
private fun CountryItem(
    country: Country,
    onCountrySelected: CountryHandler
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onCountrySelected(country) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = country.flag, fontSize = 24.sp)

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = country.name, fontSize = 16.sp)
    }

    HorizontalDivider(thickness = 1.dp)
}
