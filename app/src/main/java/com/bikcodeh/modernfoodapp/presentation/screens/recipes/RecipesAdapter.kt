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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RecipesAdapter(val onClick: (recipe: Recipe) -> Unit) :
    BaseAdapter<Recipe, ItemRecipeBinding, RecipesAdapter.RecipesViewHolder>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    ) {

    private val _isSelectedSomeItem = Channel<Boolean>(Channel.UNLIMITED)
    val isSelectedSomeItem = _isSelectedSomeItem.receiveAsFlow()

    private val _isEditing = Channel<Boolean>(Channel.UNLIMITED)
    val isEditing = _isEditing.receiveAsFlow()
    private var _isEditingItem: Boolean = false

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
            }
            setListeners(item)
        }

        private fun setListeners(recipe: Recipe) {
            binding.root.setOnClickListener {
                if (_isEditingItem) {
                    recipe.isSelected = !recipe.isSelected
                    applySelectedStyle(recipe)
                    checkSomeItemSelected()
                } else {
                    onClick(recipe)
                }
            }

            binding.root.setOnLongClickListener {
                recipe.isSelected = !recipe.isSelected
                if (!_isEditingItem) {
                    setEsEditing(!_isEditingItem)
                }
                checkSomeItemSelected()
                applySelectedStyle(recipe)
                true
            }
        }

        private fun applySelectedStyle(recipe: Recipe) {
            var strokeColor = ContextCompat.getColor(binding.root.context, R.color.strokeColor)
            var backgroundColor =
                ContextCompat.getColor(binding.root.context, R.color.cardBackgroundColor)
            if (recipe.isSelected) {
                strokeColor =
                    ContextCompat.getColor(binding.root.context, R.color.strokeColorSelected)
                backgroundColor = ContextCompat.getColor(binding.root.context, R.color.primary)
            }
            binding.recipeContainerCv.strokeColor = strokeColor
            binding.recipeContainerCv.setBackgroundColor(backgroundColor)
        }
    }

    private fun checkSomeItemSelected() {
        currentList.any { it.isSelected }.also { someSelected ->
            if (!someSelected) setEsEditing(false)
        }
    }

    fun desSelect() {
        setEsEditing(!_isEditingItem)
        currentList.forEach {
            it.isSelected = false
        }
    }

    private fun setEsEditing(editing: Boolean) {
        _isEditingItem = editing
        CoroutineScope(Dispatchers.IO).launch {
            _isEditing.send(editing)
        }
    }
}