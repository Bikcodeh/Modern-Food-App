package com.bikcodeh.modernfoodapp.util.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun Fragment.observeFlows(crossinline observationFunction: suspend (CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            observationFunction(this)
        }
    }
}
