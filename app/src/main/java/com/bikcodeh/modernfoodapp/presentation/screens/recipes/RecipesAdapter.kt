package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import android.text.Html
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.ItemRecipeBinding
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.presentation.util.BaseAdapter
import com.bikcodeh.modernfoodapp.presentation.util.BaseViewHolder
import com.bikcodeh.modernfoodapp.util.extension.loadImageFromUrl
import com.bikcodeh.modernfoodapp.util.extension.viewBinding

class RecipesAdapter(val onClick: (recipe: Recipe) -> Unit) : BaseAdapter<Recipe, ItemRecipeBinding, RecipesAdapter.RecipesViewHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
    areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val binding = parent.viewBinding(ItemRecipeBinding::inflate, false)
        return RecipesViewHolder(binding)
    }

    inner class RecipesViewHolder(viewBinding: ItemRecipeBinding) :
        BaseViewHolder<ItemRecipeBinding, Recipe>(viewBinding) {

        override fun bind(item: Recipe) {
            with(binding) {
                itemRecipeTv.text = item.title
                itemRecipeDescriptionTv.text = Html.fromHtml(item.summary).toString()
                itemRecipeLikesTv.text = item.aggregateLikes.toString()
                itemRecipeTimeTv.text = item.readyInMinutes.toString()
                itemRecipeIv.loadImageFromUrl(item.image)
                if (item.vegan) {
                    itemRecipeVeganTv.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.green
                        )
                    )
                    itemRecipeVeganIv.setColorFilter(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.green
                        )
                    )
                }
                root.setOnClickListener { onClick(item) }
            }
        }
    }
}