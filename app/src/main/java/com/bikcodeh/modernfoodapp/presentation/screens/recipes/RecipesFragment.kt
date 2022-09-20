package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentRecipesBinding
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.presentation.screens.filter.FilterState
import com.bikcodeh.modernfoodapp.presentation.screens.filter.FiltersViewModel
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.ConnectivityObserver
import com.bikcodeh.modernfoodapp.util.extension.hide
import com.bikcodeh.modernfoodapp.util.extension.hideKeyboard
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
    private var recipesAdapter = RecipesAdapter(::handleOnClick)

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel.getRecipes(filtersViewModel.applyQueries())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpListeners()
        setUpCollectors()
    }

    private fun handleOnClick(recipe: Recipe) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailFragment(recipe)
        findNavController().navigate(action)
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
                        binding.contentRecipesGroup.hide()
                        binding.errorConnectionView.root.show()
                        it.errorMessage?.let { messageId ->
                            binding.errorConnectionView.viewErrorTv.text = getString(messageId)
                        }
                        binding.errorConnectionView.viewErrorBtn.isVisible = it.displayTryAgainBtn
                    } ?: run {
                        binding.errorConnectionView.root.hide()
                    }

                    state.searchedRecipes?.let {
                        binding.contentRecipesGroup.show()
                        clearDataAndSubmit(it)
                    }

                    state.recipes?.let {
                        clearDataAndSubmit(it)
                        binding.contentRecipesGroup.show()
                    }
                }
            }

            coroutineScope.launch {
                filtersViewModel.fetchNewData.collect { filterState ->
                    when (filterState) {
                        FilterState.FetchData -> recipesViewModel.getRecipes(
                            filtersViewModel.applyQueries()
                        )
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

    private fun clearDataAndSubmit(recipes: List<Recipe>) {
        (binding.recipesRv.adapter as RecipesAdapter).apply {
            submitList(null)
            submitList(recipes)
        }
    }

    private fun setUpListeners() {
        with(binding) {
            recipesFab.setOnClickListener {
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

            errorConnectionView.viewErrorBtn.setOnClickListener {
                recipesViewModel.getRecipes(filtersViewModel.applyQueries())
            }

            searchRecipeEt.setOnEditorActionListener { textView, actionId, _ ->
                val text = textView.text.toString()
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (text.trim().count() > 3) {
                        hideKeyboard()
                        recipesViewModel.searchRecipes(filtersViewModel.applySearchQuery(text))
                    }
                }
                false
            }

            searchRecipeEt.doOnTextChanged { text, _, _, _ ->
                clearTextIvBtn.isVisible = text?.trim()?.isNotEmpty() != false
            }

            clearTextIvBtn.setOnClickListener {
                if (searchRecipeEt.text.toString().trim().isNotEmpty()) {
                    recipesViewModel.clearSearchedRecipes()
                    searchRecipeEt.setText(String())
                } else {
                    searchRecipeEt.clearFocus()
                }
            }
        }
    }
}