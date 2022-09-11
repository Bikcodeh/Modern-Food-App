package com.bikcodeh.modernfoodapp.presentation.screens.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bikcodeh.modernfoodapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FiltersBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filters_bottom_sheet, container, false)
    }
}