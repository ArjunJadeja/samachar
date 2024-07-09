package com.arjun.samachar.ui.search

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjun.samachar.App
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.databinding.FragmentSearchBinding
import com.arjun.samachar.di.component.DaggerFragmentComponent
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.ui.home.HeadlinesAdapter
import com.arjun.samachar.utils.AppConstants.DIALOG_ERROR_HEADER
import com.arjun.samachar.utils.AppConstants.DIALOG_NETWORK_ERROR
import com.arjun.samachar.utils.UiHelper
import com.arjun.samachar.utils.getQueryTextChangeStateFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var customTabsIntent: CustomTabsIntent

    @Inject
    lateinit var headlinesAdapter: HeadlinesAdapter

    private var networkConnected = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        observeNetworkStatus()
        setupHeadlinesRecyclerview()
        setupObserver()
        setupSearch()
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.searchView.getQueryTextChangeStateFlow()
                    .debounce(300)
                    .filter { it.isNotBlank() }
                    .distinctUntilChanged()
                    .collect { query ->
                        viewModel.apply {
                            updateQueryText(query = query)
                            search(query = query)
                        }
                    }
            }
        }
    }

    private fun setupHeadlinesRecyclerview() {
        headlinesAdapter.setHeadlineHandler { headline ->
            customTabsIntent.launchUrl(requireContext(), Uri.parse(headline.url))
        }
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = headlinesAdapter
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            if (it.data.isEmpty()) {
                                binding.apply {
                                    searchResultsRecyclerView.visibility = View.GONE
                                    emptyListLayout.visibility = View.VISIBLE
                                }
                            } else {
                                updateHeadlineList(it.data)
                                binding.apply {
                                    emptyListLayout.visibility = View.GONE
                                    searchResultsRecyclerView.visibility = View.VISIBLE
                                }
                            }
                        }

                        is UiState.Loading -> {
                            binding.apply {
                                searchResultsRecyclerView.visibility = View.GONE
                                progressIndicator.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Error -> {
                            binding.progressIndicator.visibility = View.GONE
                            UiHelper.showApiRetryAlert(
                                context = requireContext(),
                                header = DIALOG_ERROR_HEADER,
                                message = if (networkConnected) it.message
                                else DIALOG_NETWORK_ERROR
                            ) { viewModel.search(query = viewModel.queryText.value) }
                        }
                    }
                }
            }
        }
    }

    private fun updateHeadlineList(headlineList: List<Headline>) {
        headlinesAdapter.updateData(headlineList)
    }

    private fun observeNetworkStatus() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.isNetworkConnected.collect {
                    if (it) {
                        networkConnected = true
                        binding.apply {
                            networkConnectionStatus.visibility = View.GONE
                            networkStatusDivider.visibility = View.GONE
                        }
                    } else {
                        networkConnected = false
                        binding.apply {
                            networkConnectionStatus.visibility = View.VISIBLE
                            networkStatusDivider.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun injectDependencies() {
        DaggerFragmentComponent.builder()
            .applicationComponent((requireActivity().application as App).applicationComponent)
            .fragmentModule(FragmentModule(this@SearchFragment))
            .build()
            .inject(this@SearchFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}