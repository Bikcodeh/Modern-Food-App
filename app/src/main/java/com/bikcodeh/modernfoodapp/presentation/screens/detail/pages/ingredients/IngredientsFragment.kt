package com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.ingredients

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.databinding.FragmentIngredientsBinding
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding

class IngredientsFragment :
    BaseFragmentBinding<FragmentIngredientsBinding>(FragmentIngredientsBinding::inflate) {

    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        val recipe: Recipe? = arguments?.getParcelable("recipeBundle")
        recipe?.let {
            ingredientsAdapter.submitList(recipe.extendedIngredients)
        } ?: run {
            findNavController().popBackStack()
        }
    }

    private fun setUpRecyclerView() {
        binding.ingredientsRv.apply {
            adapter = ingredientsAdapter
        }
    }
}