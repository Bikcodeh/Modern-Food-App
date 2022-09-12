package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentRecipesBinding
import com.bikcodeh.modernfoodapp.presentation.screens.filter.FilterState
import com.bikcodeh.modernfoodapp.presentation.screens.filter.FiltersViewModel
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.ConnectivityObserver
import com.bikcodeh.modernfoodapp.util.extension.hide
import com.bikcodeh.modernfoodapp.util.extension.observeFlows
import com.bikcodeh.modernfoodapp.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipesFragment :
    BaseFragmentBinding<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private val recipesViewModel by viewModels<RecipesViewModel>()
    private val filtersViewModel by activityViewModels<FiltersViewModel>()
    private val recipesAdapter by lazy { RecipesAdapter() }

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpListeners()
        setUpCollectors()
    }

    private fun setUpViews() {
        binding.recipesRv.apply {
            adapter = recipesAdapter
        }
    }

    private fun setUpCollectors() {
        observeFlows { coroutineScope ->
            coroutineScope.launch {
                recipesViewModel.recipesState.collect { state ->
                    if (state.isLoading) {
                        binding.contentRecipesGroup.hide()
                        binding.recipesLoadingSv.show()
                    } else {
                        binding.recipesLoadingSv.hide()
                    }

                    state.error?.let {
                        binding.errorConnectionView.root.show()
                        it.errorMessage?.let { messageId ->
                            binding.errorConnectionView.viewErrorTv.text = getString(messageId)
                        }
                        binding.errorConnectionView.viewErrorBtn.isVisible = it.displayTryAgainBtn
                    } ?: run {
                        binding.errorConnectionView.root.hide()
                    }
                }
            }

            coroutineScope.launch {
                recipesViewModel.recipes.collect {
                    if (it.isEmpty()) {
                        recipesViewModel.getRecipes(filtersViewModel.applyQueries())
                    } else {
                        binding.contentRecipesGroup.show()
                    }
                    recipesAdapter.submitList(it)
                }
            }

            coroutineScope.launch {
                filtersViewModel.fetchNewData.collect { filterState ->
                    when (filterState) {
                        FilterState.FetchData -> recipesViewModel.getRecipes(filtersViewModel.applyQueries())
                    }
                }
            }

            coroutineScope.launch {
                connectivityObserver.observe().collect {
                    when (it) {
                        ConnectivityObserver.Status.Available -> recipesViewModel.setNavigateToFilter(
                            true
                        )
                        ConnectivityObserver.Status.Unavailable -> recipesViewModel.setNavigateToFilter(
                            false
                        )
                        ConnectivityObserver.Status.Losing -> {}
                        ConnectivityObserver.Status.Lost -> recipesViewModel.setNavigateToFilter(
                            false
                        )
                    }
                }
            }
        }

    }

    private fun setUpListeners() {
        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.canNavigateToFilter) {
                findNavController().navigate(R.id.action_recipesFragment_to_filtersBottomSheetFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.no_internet_connection,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.errorConnectionView.viewErrorBtn.setOnClickListener {
            recipesViewModel.getRecipes(filtersViewModel.applyQueries())
        }
    }
}