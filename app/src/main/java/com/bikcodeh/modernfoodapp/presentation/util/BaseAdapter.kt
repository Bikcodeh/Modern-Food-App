package com.bikcodeh.modernfoodapp.presentation.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseAdapter<T : Any, V : ViewBinding, VH : BaseViewHolder<V, T>>(
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
) : ListAdapter<T, VH>(
    BaseDiffUtil<T>(
        areItemsTheSame, areContentsTheSame
    )
) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}

class BaseDiffUtil<T : Any>(
    val areItemsTheSameCallBack: (oldItem: T, newItem: T) -> Boolean,
    val areContentsTheSameCallback: (oldItem: T, newItem: T) -> Boolean
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return areItemsTheSameCallBack(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return areContentsTheSameCallback(oldItem, newItem)
    }
}

abstract class BaseViewHolder<V : ViewBinding, T : Any>(val binding: V) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: T)
}