package com.lambda_labs.community_calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.lambda_labs.community_calendar.util.DatePickerFragment
import com.lambda_labs.community_calendar.util.Util
import kotlinx.android.synthetic.main.fragment_filter.*


class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image_view_fragment_filter_cancel.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        // Populate list of locations in the Spinner View
        ArrayAdapter.createFromResource(
            view.context,
            R.array.filter_locations_array,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_fragment_filter_location.adapter = arrayAdapter
        }

        // Show the Date Picker when the date control is engaged
        text_view_fragment_filter_date_shown.text = Util.getSearchDate(Util.getToday())
        image_view_fragment_filter_date.setOnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.show(fragmentManager!!, "datePicker")
        }

        // Populate the initial chip tags to be added to the included group
        resources.getStringArray(R.array.tags_added_array).forEachIndexed { index, tagText ->
            val chip: Chip = generateChip()
            chip.text = tagText
            chip.id = index
            chip_group_fragment_filter_added.addView(chip)
        }

        // Populate the initial chip tags to be added to the suggested group
        resources.getStringArray(R.array.tags_suggested_array).forEachIndexed { index, tagText ->
            val chip: Chip = generateChip()
            chip.text = tagText
            chip.id = index
            chip_group_fragment_filter_suggested.addView(chip)
            //TODO: Change chip attributes for suggestions
        }
    }

    private fun generateChip(): Chip {
        val chip: Chip = Chip(context)
        chip.isClickable = false
        chip.isCheckable = false
        chip.isChipIconVisible = true
        chip.isCloseIconVisible = true
        chip.isCheckedIconVisible = false
        chip.isCheckedIconVisible = false
        chip.textSize = 13f
        chip.typeface = ResourcesCompat.getFont(context!!, R.font.poppins_regular)
        chip.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util.dpToPx(40))
        chip.setCloseIconTintResource(R.color.colorInactive)
        chip.setCloseIconResource(R.drawable.ic_cancel_circle_24dp)
        chip.setChipBackgroundColorResource(android.R.color.black)
        chip.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))

        return chip
    }
}
