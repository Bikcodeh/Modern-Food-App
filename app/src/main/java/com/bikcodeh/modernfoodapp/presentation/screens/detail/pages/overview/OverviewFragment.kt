package com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.overview

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentOverviewBinding
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.extension.loadImageFromUrl
import org.jsoup.Jsoup

class OverviewFragment :
    BaseFragmentBinding<FragmentOverviewBinding>(FragmentOverviewBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipe: Recipe? = arguments?.getParcelable("recipeBundle")
        setUpViews(recipe)
    }

    private fun setUpViews(recipe: Recipe?) {
        recipe?.let {
            with(binding) {
                mainImageView.loadImageFromUrl(it.image)
                titleTextView.text = it.title
                likesTextView.text = it.aggregateLikes.toString()
                timeTextView.text = it.readyInMinutes.toString()
                summaryTextView.text = Jsoup.parse(it.summary).text()
                if (it.vegan) setToActive(veganTextView, veganImageView)
                if (it.vegetarian) setToActive(vegetarianTextView, vegetarianImageView)
                if (it.glutenFree) setToActive(glutenFreeTextView, glutenFreeImageView)
                if (it.dairyFree) setToActive(dairyFreeTextView, dairyFreeImageView)
                if (it.veryHealthy) setToActive(healthyTextView, healthyImageView)
                if (it.cheap) setToActive(cheapTextView, cheapImageView)
            }
        } ?: run {
            findNavController().popBackStack()
        }
    }

    private fun setToActive(textView: TextView, imageView: ImageView) {
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
    }
}