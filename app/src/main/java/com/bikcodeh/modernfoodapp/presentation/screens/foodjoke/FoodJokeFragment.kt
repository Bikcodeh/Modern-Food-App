package com.bikcodeh.modernfoodapp.presentation.screens.foodjoke

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentFoodJokeBinding
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.extension.hide
import com.bikcodeh.modernfoodapp.util.extension.observeFlows
import com.bikcodeh.modernfoodapp.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment :
    BaseFragmentBinding<FragmentFoodJokeBinding>(FragmentFoodJokeBinding::inflate) {

    private val foodJokeViewModel by viewModels<FoodJokeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCollectors()
        setUpListeners()
        foodJokeViewModel.getRemoteFoodJoke()
    }

    private fun setUpCollectors() {
        observeFlows { coroutineScope ->
            coroutineScope.launch {
                foodJokeViewModel.foodJokeState.collect { state ->
                    with(binding) {
                        foodJokePb.isVisible = state.isLoading
                        state.joke?.let {
                            foodJokeCv.show()
                            if (it.isEmpty()) {
                                handleEmptyFoodJoke(state.error?.errorMessage)
                            } else {
                                foodJokeTv.text = it
                                foodJokeViewModel.setJoke(it)
                            }
                        }
                        state.error?.let {
                            foodJokeViewModel.getLocalFoodJokes()
                        } ?: run {
                            errorConnectionView.root.hide()
                        }
                    }
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.errorConnectionView.viewErrorBtn.setOnClickListener {
            foodJokeViewModel.getRemoteFoodJoke()
        }

        binding.foodJokeShare.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, foodJokeViewModel.foodJoke)
                type = "text/plain"
            }.also {
                startActivity(it)
            }
        }
    }

    private fun handleEmptyFoodJoke(message: Int?) {
        with(binding) {
            foodJokeCv.hide()
            errorConnectionView.root.show()
            errorConnectionView.viewErrorBtn.isVisible = false
            errorConnectionView.viewErrorTv.text = getString(message ?: R.string.unknown_error)
        }
    }
}