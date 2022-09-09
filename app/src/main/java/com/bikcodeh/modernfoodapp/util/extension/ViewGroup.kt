package com.bikcodeh.modernfoodapp.util.extension

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> ViewGroup.viewBinding(
    crossinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    attachToParent: Boolean = true
): T = bindingInflater.invoke(LayoutInflater.from(this.context), this, attachToParent)