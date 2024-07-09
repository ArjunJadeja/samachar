package com.arjun.samachar.ui.home

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
import androidx.recyclerview.widget.RecyclerView
import com.arjun.samachar.App
import com.arjun.samachar.R
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.databinding.FragmentHomeBinding
import com.arjun.samachar.di.component.DaggerFragmentComponent
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.utils.AppConstants.DEFAULT_COUNTRY_CODE
import com.arjun.samachar.utils.AppConstants.DIALOG_ERROR_HEADER
import com.arjun.samachar.utils.AppConstants.SCROLLING_THRESH_HOLD
import com.arjun.samachar.utils.UiHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var headlinesAdapter: HeadlinesAdapter

    @Inject
    lateinit var customTabsIntent: CustomTabsIntent

    private var networkConnected = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        observeNetworkStatus()
        setupHeadlinesRecyclerview()
        observeNewsList()
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getHeadlinesByCountry(countryCode = DEFAULT_COUNTRY_CODE)
                swipeRefreshLayout.isRefreshing = false
            }
            backToTopButton.setOnClickListener {
                headlinesRecyclerView.smoothScrollToPosition(0)
            }
            searchNews.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }
    }

    private fun observeNewsList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.headlineList.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            if (it.data.isEmpty()) {
                                binding.apply {
                                    swipeRefreshLayout.visibility = View.GONE
                                    emptyListLayout.visibility = View.VISIBLE
                                }
                            } else {
                                updateHeadlineList(it.data)
                                binding.apply {
                                    emptyListLayout.visibility = View.GONE
                                    swipeRefreshLayout.visibility = View.VISIBLE
                                }
                            }
                        }

                        is UiState.Loading -> {
                            binding.apply {
                                swipeRefreshLayout.visibility = View.GONE
                                progressIndicator.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Error -> {
                            binding.apply {
                                progressIndicator.visibility = View.GONE
                                swipeRefreshLayout.visibility = View.GONE
                            }
                            UiHelper.showApiRetryAlert(
                                context = requireContext(),
                                header = DIALOG_ERROR_HEADER,
                                message = it.message
                            ) { viewModel.getHeadlinesByCountry(countryCode = DEFAULT_COUNTRY_CODE) }
                        }
                    }
                }
            }
        }
    }

    private fun updateHeadlineList(headlineList: List<Headline>) {
        headlinesAdapter.updateData(headlineList)
    }

    private fun setupHeadlinesRecyclerview() {
        headlinesAdapter.setHeadlineHandler { headline ->
            customTabsIntent.launchUrl(requireContext(), Uri.parse(headline.url))
        }
        binding.apply {
            headlinesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            headlinesRecyclerView.adapter = headlinesAdapter
            headlinesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (dy > 0 && firstVisibleItemPosition > SCROLLING_THRESH_HOLD) {
                        backToTopButton.visibility = View.VISIBLE
                    } else if (dy < 0) {
                        backToTopButton.visibility = View.GONE
                    }
                }
            })
        }
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
            .fragmentModule(FragmentModule(this@HomeFragment))
            .build()
            .inject(this@HomeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}