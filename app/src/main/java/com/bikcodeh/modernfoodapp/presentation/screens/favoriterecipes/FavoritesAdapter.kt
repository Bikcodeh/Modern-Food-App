package com.bikcodeh.modernfoodapp.presentation.screens.favoriterecipes

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

class FavoritesAdapter(val onClick: (recipe: Recipe) -> Unit) :
    BaseAdapter<Recipe, ItemRecipeBinding, FavoritesAdapter.RecipesViewHolder>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    ) {

    private val _isEditing = Channel<Boolean>(Channel.UNLIMITED)
    val isEditing = _isEditing.receiveAsFlow()

    private var _isEditingItem: Boolean = false

    private val _totalSelected: Channel<Int> = Channel(Channel.UNLIMITED)
    val totalSelected = _totalSelected.receiveAsFlow()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesAdapter.RecipesViewHolder {
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
                applySelectedStyle(item)
            }
            setListeners()
        }

        private fun setListeners() {
            binding.root.setOnClickListener {
                if (_isEditingItem) {
                    currentList[adapterPosition].isSelected = !currentList[adapterPosition].isSelected
                    val total = getTotalSelected()
                    CoroutineScope(Dispatchers.IO).launch {
                        _totalSelected.send(total)
                    }
                    applySelectedStyle(currentList[adapterPosition])
                    checkSomeItemSelected()
                } else {
                    onClick(currentList[adapterPosition])
                }
            }

            binding.root.setOnLongClickListener {
                currentList[adapterPosition].isSelected = !currentList[adapterPosition].isSelected
                if (!_isEditingItem) {
                    setIsEditing(true)
                }
                applySelectedStyle(currentList[adapterPosition])
                val total = getTotalSelected()
                CoroutineScope(Dispatchers.IO).launch {
                    _totalSelected.send(total)
                }
                checkSomeItemSelected()
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
            binding.itemRecipeContainerCnl.setBackgroundColor(backgroundColor)
        }
    }

    private fun checkSomeItemSelected() {
        currentList.count { it.isSelected }.also {
            if (it == 0) {
                setIsEditing(false)
            }
        }

    }

    fun getTotalSelected(): Int {
        return currentList.count { it.isSelected }
    }

    fun getRecipeSelected(): List<Recipe> = currentList.filter { it.isSelected }

    fun desSelect() {
        currentList.mapIndexed { index, recipe ->
            if (recipe.isSelected) {
                recipe.isSelected = false
                notifyItemChanged(index)
            }
        }
        setIsEditing(false)
    }

    fun setIsEditing(editing: Boolean) {
        _isEditingItem = editing
        CoroutineScope(Dispatchers.IO).launch {
            _isEditing.send(editing)
        }
    }
}