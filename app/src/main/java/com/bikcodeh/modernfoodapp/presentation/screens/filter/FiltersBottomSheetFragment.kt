package com.bikcodeh.modernfoodapp.presentation.screens.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.databinding.FragmentFiltersBottomSheetBinding
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_DIET_TYPE
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_MEAL_TYPE
import com.bikcodeh.modernfoodapp.util.extension.observeFlows
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FiltersBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFiltersBottomSheetBinding? = null
    private val binding: FragmentFiltersBottomSheetBinding
        get() = _binding!!

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0


    private val filtersViewModel by activityViewModels<FiltersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
        setUpCollectors()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpCollectors() {
        observeFlows { coroutineScope ->

            coroutineScope.launch {
                filtersViewModel.filters.collect {
                    mealTypeChip = it.selectedMealType
                    mealTypeChipId = it.selectedMealTypeId
                    dietTypeChip = it.selectedDietType
                    dietTypeChipId = it.selectedDietTypeId
                    updateChip(
                        it.selectedMealTypeId,
                        binding.mealTypeChipGroup,
                        binding.mealTypeScrollView
                    )
                    updateChip(
                        it.selectedDietTypeId,
                        binding.dietTypeChipGroup,
                        binding.dietTypeScrollView
                    )
                }
            }
        }
    }

    private fun setUpListeners() {

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase()
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase()
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }


        binding.applyBtn.setOnClickListener {
            filtersViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            filtersViewModel.setFetchNewData()
            val action =
                FiltersBottomSheetFragmentDirections.actionFiltersBottomSheetFragmentToRecipesFragment(
                    true
                )
            findNavController().navigate(action)
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup, scroll: HorizontalScrollView) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
                setScrollPosition(chipId, chipGroup, scroll)
            } catch (e: Exception) {
                Timber.e("RecipesBottomSheet", e.message.toString())
            }
        }
    }

    private fun setScrollPosition(chipId: Int, chipGroup: ChipGroup, scroll: HorizontalScrollView) {
        val chip = chipGroup.findViewById<Chip>(chipId)
        scroll.scrollTo(chip.left, chip.right)
    }
}