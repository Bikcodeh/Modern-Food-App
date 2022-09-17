package com.bikcodeh.modernfoodapp.presentation.screens.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bikcodeh.modernfoodapp.domain.model.Recipe

class DetailPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment, recipe: Recipe) {
        fragment.arguments = Bundle().apply { putParcelable("recipeBundle", recipe) }
        fragments.add(fragment)
    }

    override fun getItemCount(): Int = fragments.count()

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}