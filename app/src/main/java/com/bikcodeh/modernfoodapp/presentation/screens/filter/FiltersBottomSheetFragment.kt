package com.bikcodeh.modernfoodapp.presentation.screens.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.databinding.FragmentFiltersBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FiltersBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFiltersBottomSheetBinding? = null
    private val binding: FragmentFiltersBottomSheetBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}