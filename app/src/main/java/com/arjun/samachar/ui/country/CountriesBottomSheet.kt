package com.arjun.samachar.ui.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjun.samachar.App
import com.arjun.samachar.data.model.Country
import com.arjun.samachar.databinding.BottomSheetCountriesBinding
import com.arjun.samachar.di.component.DaggerFragmentComponent
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.UiState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountriesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCountriesBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var viewModel: CountriesViewModel

    @Inject
    lateinit var countriesAdapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCountriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "CountriesBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        setupCountriesRecyclerview()
        observeCountryList()
    }

    private fun observeCountryList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countryList.collect {
                    when (it) {
                        is UiState.Success -> {
                            updateCountryList(it.data)
                            binding.countriesRecyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.countriesRecyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            this@CountriesBottomSheet.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun updateCountryList(countries: List<Country>) {
        countriesAdapter.updateData(countries)
    }

    private fun setupCountriesRecyclerview() {
        countriesAdapter.setCountryHandler { country ->
            mainViewModel.apply {
                clearSelectedLanguage()
                clearSelectedSource()
                updateSelectedCountry(country)
            }
            this@CountriesBottomSheet.dismiss()
        }
        binding.countriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countriesAdapter
        }
    }

    private fun injectDependencies() {
        DaggerFragmentComponent.builder()
            .applicationComponent((requireActivity().application as App).applicationComponent)
            .fragmentModule(FragmentModule(this@CountriesBottomSheet))
            .build()
            .inject(this@CountriesBottomSheet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}