package com.bikcodeh.modernfoodapp.presentation.screens.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentDetailBinding
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.IngredientsFragment
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.InstructionsFragment
import com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.OverviewFragment
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : BaseFragmentBinding<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private lateinit var adapter: DetailPagerAdapter

    private val tabTitles = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DetailPagerAdapter(parentFragmentManager, viewLifecycleOwner.lifecycle)
        setUpTabTitles()
        setUpAdapter()
        setUpViewPager()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.detailBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpAdapter() {
        adapter.addFragment(OverviewFragment())
        adapter.addFragment(IngredientsFragment())
        adapter.addFragment(InstructionsFragment())
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