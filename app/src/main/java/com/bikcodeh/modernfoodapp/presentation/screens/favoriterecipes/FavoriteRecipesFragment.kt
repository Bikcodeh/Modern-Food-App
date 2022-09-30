package com.bikcodeh.modernfoodapp.presentation.screens.favoriterecipes

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentFavoriteRecipesBinding
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.presentation.screens.recipes.RecipesViewModel
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.extension.hide
import com.bikcodeh.modernfoodapp.util.extension.observeFlows
import com.bikcodeh.modernfoodapp.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteRecipesFragment :
    BaseFragmentBinding<FragmentFavoriteRecipesBinding>(FragmentFavoriteRecipesBinding::inflate) {

    private val recipesViewModel by viewModels<RecipesViewModel>()
    private var recipesAdapter = FavoritesAdapter(::handleOnClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (recipesViewModel._isEditing) {
                    recipesAdapter.desSelect()
                } else {
                    findNavController().popBackStack()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteRv.apply {
            adapter = recipesAdapter
        }
        setUpCollectors()
        setListeners()
    }

    private fun handleOnClick(recipe: Recipe) {
        val action =
            FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailFragment(recipe)
        findNavController().navigate(action)
    }

    private fun setUpCollectors() {
        observeFlows { coroutineScope ->
            coroutineScope.launch {
                recipesViewModel.favoriteRecipes.collect {
                    if (!recipesViewModel._isEditing) {
                        recipesAdapter.submitList(it)
                    }
                    if (it.isNotEmpty()) {
                        binding.emptyFavoritesView.root.hide()
                        binding.favoriteRv.show()
                    } else {
                        binding.emptyFavoritesView.root.show()
                        binding.favoriteRv.hide()
                    }
                }
            }

            coroutineScope.launch {
                recipesAdapter.isEditing.collect { isEditing ->
                    recipesViewModel.setIsEditing(isEditing)
                    if (isEditing) {
                        binding.layoutSelectItems.root.show()
                    } else {
                        binding.layoutSelectItems.root.hide()
                    }
                }
            }

            coroutineScope.launch {
                recipesAdapter.totalSelected.collect {
                    if (it >= 1) {
                        binding.layoutSelectItems.itemSelectedTv.text =
                            resources.getQuantityString(R.plurals.total_selected, it, it)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipesViewModel.setIsEditing(false)
    }

    private fun setListeners() {
        binding.layoutSelectItems.closeMenuIb.setOnClickListener {
            recipesAdapter.desSelect()
        }
    }
}