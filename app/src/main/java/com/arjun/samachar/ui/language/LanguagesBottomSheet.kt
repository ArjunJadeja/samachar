package com.arjun.samachar.ui.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arjun.samachar.App
import com.arjun.samachar.data.model.Language
import com.arjun.samachar.databinding.BottomSheetLanguageBinding
import com.arjun.samachar.di.component.DaggerFragmentComponent
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.UiState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguagesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLanguageBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var viewModel: LanguageViewModel

    @Inject
    lateinit var languagesAdapter: LanguagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "LanguagesBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        setupLanguagesRecyclerView()
        observeLanguageList()
        binding.selectLanguageButton.setOnClickListener {
            this@LanguagesBottomSheet.dismiss()
        }
    }

    private fun observeLanguageList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.languageList.collect {
                    when (it) {
                        is UiState.Success -> {
                            updateLanguageList(it.data)
                            binding.languagesListRecyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.languagesListRecyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            this@LanguagesBottomSheet.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun updateLanguageList(languageList: List<Language>) {
        languagesAdapter.updateData(languageList)
    }

    private fun setupLanguagesRecyclerView() {
        languagesAdapter.apply {
            setSelectedLanguage(mainViewModel.headlinesParams.value.selectedLanguageCode)
            setLanguageHandler { selectedLanguage ->
                mainViewModel.apply {
                    clearSelectedLanguage()
                    updateSelectedLanguage(selectedLanguage.code)
                }
            }
        }
        binding.languagesListRecyclerView.adapter = languagesAdapter
    }

    private fun injectDependencies() {
        DaggerFragmentComponent.builder()
            .applicationComponent((requireActivity().application as App).applicationComponent)
            .fragmentModule(FragmentModule(this@LanguagesBottomSheet))
            .build()
            .inject(this@LanguagesBottomSheet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}