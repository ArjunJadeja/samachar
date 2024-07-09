package com.arjun.samachar.ui.source

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
import com.arjun.samachar.data.model.Source
import com.arjun.samachar.databinding.BottomSheetSourcesBinding
import com.arjun.samachar.di.component.DaggerFragmentComponent
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.UiState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourcesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSourcesBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var viewModel: SourcesViewModel

    @Inject
    lateinit var sourcesAdapter: SourcesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "SourcesBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        getSources()
        setupSourcesRecyclerview()
        observeSourceList()
    }

    private fun observeSourceList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sourceList.collect {
                    when (it) {
                        is UiState.Success -> {
                            updateSourceList(it.data)
                            binding.sourcesRecyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.sourcesRecyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            this@SourcesBottomSheet.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun updateSourceList(articleList: List<Source>) {
        sourcesAdapter.updateData(articleList)
    }

    private fun setupSourcesRecyclerview() {
        sourcesAdapter.setSourceHandler { source ->
            mainViewModel.apply {
                clearSelectedLanguage()
                updateSelectedSource(source.id.toString())
            }
            this@SourcesBottomSheet.dismiss()
        }
        binding.sourcesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sourcesAdapter
        }
    }

    private fun getSources() {
        viewModel.getSources(mainViewModel.headlinesParams.value.selectedCountry.code)
    }

    private fun injectDependencies() {
        DaggerFragmentComponent.builder()
            .applicationComponent((requireActivity().application as App).applicationComponent)
            .fragmentModule(FragmentModule(this@SourcesBottomSheet))
            .build()
            .inject(this@SourcesBottomSheet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}