package com.bikcodeh.modernfoodapp

import android.os.Bundle
import android.view.View
import com.bikcodeh.modernfoodapp.databinding.FragmentDetailBinding
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding

class DetailFragment : BaseFragmentBinding<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}