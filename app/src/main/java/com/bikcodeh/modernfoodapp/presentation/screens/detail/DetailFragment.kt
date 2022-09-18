package com.bikcodeh.modernfoodapp.presentation.screens.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentDetailBinding
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.ingredients.IngredientsFragment
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.instructions.InstructionsFragment
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.overview.OverviewFragment
import com.bikcodeh.modernfoodapp.presentation.screens.recipes.RecipesViewModel
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

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
        setUpTabTitles()
        setUpAdapter()
        setUpViewPager()
        setUpListeners()
        setUpFavorite()
    }

    private fun setUpFavorite() {
        if (args.recipe.isFavorite) binding.favoriteIcon.progress = 0.5f
        checked = args.recipe.isFavorite
    }

    private fun setUpListeners() {

        with(binding) {
            detailBack.setOnClickListener {
                findNavController().popBackStack()
            }

            favoriteIcon.setOnClickListener {
                checked = if (checked) {
                    favoriteIcon.setMinAndMaxProgress(0.5f, 1.0f)
                    favoriteIcon.playAnimation()
                    false
                } else {
                    favoriteIcon.setMinAndMaxProgress(0.0f, 0.5f)
                    favoriteIcon.playAnimation()
                    true
                }
                recipesViewModel.setAsFavorite(checked, args.recipe.id)
            }
        }
    }

    private fun setUpAdapter() {
        adapter.addFragment(OverviewFragment(), args.recipe)
        adapter.addFragment(IngredientsFragment(), args.recipe)
        adapter.addFragment(InstructionsFragment(), args.recipe)
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