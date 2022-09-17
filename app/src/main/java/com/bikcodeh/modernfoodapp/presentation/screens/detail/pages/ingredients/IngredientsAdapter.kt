package com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.ingredients

import android.view.ViewGroup
import com.bikcodeh.modernfoodapp.databinding.ItemIngredientsBinding
import com.bikcodeh.modernfoodapp.domain.model.ExtendedIngredient
import com.bikcodeh.modernfoodapp.presentation.util.BaseAdapter
import com.bikcodeh.modernfoodapp.presentation.util.BaseViewHolder
import com.bikcodeh.modernfoodapp.util.Constants.BASE_IMAGE_URL
import com.bikcodeh.modernfoodapp.util.extension.loadImageFromUrl
import com.bikcodeh.modernfoodapp.util.extension.viewBinding

class IngredientsAdapter :
    BaseAdapter<ExtendedIngredient, ItemIngredientsBinding, IngredientsAdapter.IngredientsViewHolder>(
        areItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    ) {

    inner class IngredientsViewHolder(viewBinding: ItemIngredientsBinding) :
        BaseViewHolder<ItemIngredientsBinding, ExtendedIngredient>(viewBinding) {

        override fun bind(item: ExtendedIngredient) {
            with(binding) {
                ingredientImageView.loadImageFromUrl(item.image)
                ingredientName.text = item.name.replaceFirstChar { it.uppercase() }
                ingredientAmount.text = item.amount.toString()
                ingredientUnit.text = item.unit
                ingredientConsistency.text = item.consistency
                ingredientOriginal.text = item.original
                ingredientImageView.loadImageFromUrl(BASE_IMAGE_URL + item.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding = parent.viewBinding(ItemIngredientsBinding::inflate, false)
        return IngredientsViewHolder(binding)
    }
}