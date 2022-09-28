package com.bikcodeh.modernfoodapp.presentation.screens.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentDetailBinding
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.ingredients.IngredientsFragment
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.instructions.InstructionsFragment
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.overview.OverviewFragment
import com.bikcodeh.modernfoodapp.presentation.screens.recipes.RecipesViewModel
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.extension.observeFlows
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragmentBinding<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private lateinit var adapter: DetailPagerAdapter

    private val tabTitles = mutableListOf<String>()

    private val args by navArgs<DetailFragmentArgs>()

    private val recipesViewModel: RecipesViewModel by viewModels()

    private var checked: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DetailPagerAdapter(parentFragmentManager, viewLifecycleOwner.lifecycle)
        setUpCollectors()
        recipesViewModel.getRecipeById(args.recipe.id)
    }

    private fun setUpFavorite(recipe: Recipe) {
        if (recipe.isFavorite) binding.favoriteIcon.progress = 0.5f
        checked = recipe.isFavorite
    }

    private fun setUpListeners(recipeId: Int) {

        with(binding) {
            detailBack.setOnClickListener {
                findNavController().popBackStack()
            }

            favoriteIcon.setOnClickListener {
                checked = if (checked) {
                    recipesViewModel.insertRecipe(args.recipe)
                    favoriteIcon.setMinAndMaxProgress(0.5f, 1.0f)
                    favoriteIcon.playAnimation()
                    false
                } else {
                    favoriteIcon.setMinAndMaxProgress(0.0f, 0.5f)
                    favoriteIcon.playAnimation()
                    true
                }
                recipesViewModel.setAsFavorite(checked, recipeId)
            }
        }
    }

    private fun setUpCollectors() {
        observeFlows { coroutineScope ->
            coroutineScope.launch {
                recipesViewModel.recipeDetail.collect { state ->
                    when (state) {
                        DetailState.Idle -> {}
                        is DetailState.Success -> {
                            state.recipe?.let { recipe ->
                                setUpFlow(recipe)
                            } ?: run {
                                setUpFlow(args.recipe)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpFlow(recipe: Recipe) {
        setUpFavorite(recipe)
        setUpListeners(recipe.id)
        setUpAdapter(recipe)
        setUpTabTitles()
        setUpViewPager()
    }

    private fun setUpAdapter(recipe: Recipe) {
        adapter.addFragment(OverviewFragment(), recipe)
        adapter.addFragment(IngredientsFragment(), recipe)
        adapter.addFragment(InstructionsFragment(), recipe)
    }

    private fun setUpViewPager() {
        with(binding) {
            detailVp.adapter = adapter
            TabLayoutMediator(detailTabs, detailVp) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

    private fun setUpTabTitles() {
        tabTitles.add(getString(R.string.overview_tab_title))
        tabTitles.add(getString(R.string.ingredients_tab_title))
        tabTitles.add(getString(R.string.instructions_tab_title))
    }
}