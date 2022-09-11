package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentRecipesBinding
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.extension.hide
import com.bikcodeh.modernfoodapp.util.extension.observeFlows
import com.bikcodeh.modernfoodapp.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment :
    BaseFragmentBinding<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private val recipesViewModel by viewModels<RecipesViewModel>()
    private val recipesAdapter by lazy { RecipesAdapter() }

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
                        binding.recipesLoadingSv.show()
                    } else {
                        binding.recipesLoadingSv.hide()
                    }

                    if (state.hasError) {
                        binding.contentRecipesGroup.hide()
                        binding.errorConnectionView.root.show()
                    } else {
                        binding.errorConnectionView.root.hide()
                    }
                }
            }

            recipesViewModel.recipes.collect {
                recipesAdapter.submitList(it)
                binding.contentRecipesGroup.show()
                if (it.isEmpty()) {
                    recipesViewModel.getRecipes()
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_filtersBottomSheetFragment)
        }
    }
}