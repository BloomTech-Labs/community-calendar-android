package com.lambda_labs.community_calendar.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.model.Filter
import com.lambda_labs.community_calendar.util.*
import com.lambda_labs.community_calendar.viewmodel.SharedFilterViewModel
import kotlinx.android.synthetic.main.fragment_filter.*
import java.util.*


class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reserve a variable for the context
        lateinit var fragContext: Context

        // Ensure the context is not null before assigning it
        context?.let {
            fragContext = it
        } ?: run {
            Log.e("FilterFragment", "Failed to assign context!")
        }

        // Enable touch event on this fragment in order to prevent views beneath from responding
        view.setOnTouchListener { v, event ->
            return@setOnTouchListener true
        }


        //TODO: ViewModel access to events


        // Activate the image of the X in the upper left, in effect to cancel, discarding changes
        image_view_fragment_filter_cancel.setOnClickListener {
            // Dismiss the soft keyboard if it is showing
            hideKeyboard(fragContext as MainActivity)
            // Dismiss this fragment, after having saved any user selections
            Navigation.findNavController(it).popBackStack()
        }

        // Populate list of locations in the Spinner View
        // TODO: Change this to locations from ViewModel's event list
        val temporaryHardcodedListOfLocations = listOf<String>(
            "",
            "West Side",
            "East Side",
            "Southwest Detroit",
            "Palmer Park Area",
            "North End",
            "New Center Area",
            "Eastern Market Area",
            "Midtown",
            "Jefferson Corridor",
            "Downtown",
            "Corktown - Woodbridge"
        )
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            fragContext,
            android.R.layout.simple_spinner_item,
            temporaryHardcodedListOfLocations
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_fragment_filter_location.adapter = it
        }


        // Show the Date Picker when the date control is engaged
        image_view_fragment_filter_date.setOnClickListener {
            var selectedDate: String = text_view_fragment_filter_date_shown.text.toString()

            // Ensure the date string has a valid value before passing it to the DatePicker
            if (selectedDate.isBlank()) selectedDate = getSearchDate(getToday())

            val date: Date = searchStringToDate(selectedDate)
            val datePicker = DatePickerFragment(fragContext, toCalendar(date))
            datePicker.show(childFragmentManager, "datePicker")
        }


        // Instantiate the shared ViewModel to allow user selections to persist after fragment destruction
        val sharedFilterViewModel: SharedFilterViewModel =
            ViewModelProviders.of(fragContext as MainActivity)[SharedFilterViewModel::class.java]
        sharedFilterViewModel.getSharedData()
            .observe(viewLifecycleOwner, Observer<Filter> { filter ->
                setUpSelections(filter)
            })


        // Populate the initial chip tags to be added to the included group
        sharedFilterViewModel.getSharedData().value?.tags?.forEachIndexed { index, tagText ->
            addChip(fragContext, tagText, index)
        }

        // Populate the initial chip tags to be added to the suggested group
        // TODO: Change this to tags from the ViewModel's event list
        val temporaryHardcodedListOfTags = mutableListOf<String>(
            "Tech",
            "Entertainment",
            "Gardening",
            "Sewing",
            "Sports",
            "Outdoors",
            "Music",
            "Family",
            "Fun",
            "Eating"
        )
        temporaryHardcodedListOfTags.shuffle()
        for (x in 0 until temporaryHardcodedListOfTags.size) {
            val chip: Chip = ViewUtil.generateChip(fragContext, false)
            chip.text = temporaryHardcodedListOfTags[x]
            chip.id = x
            chip.setOnCloseIconClickListener {
                chip_group_fragment_filter_suggested.removeView(it)
                val chipChange: Chip = ViewUtil.generateChip(fragContext, true)
                chipChange.text = (it as Chip).text
                chipChange.id = chip_group_fragment_filter_added.childCount + 1
                chipChange.setOnCloseIconClickListener {
                    chip_group_fragment_filter_added.removeView(chipChange)
                }
                chip_group_fragment_filter_added.addView(chipChange)
            }
            chip_group_fragment_filter_suggested.addView(chip)
        }

        // Populate the SearchView suggestions with a list of tags and when selected, gets added
        // TODO: Change this to tags from the ViewModel's event list
        val searchAutoComplete =
            search_bar_fragment_filter.findViewById<View>(androidx.appcompat.R.id.search_src_text) as SearchAutoComplete
        searchAutoComplete.dropDownAnchor = R.id.search_bar_fragment_filter
        searchAutoComplete.threshold = 1
        searchAutoComplete.setOnItemClickListener { parent, view1, position, id ->
            val selected: String = (view1 as MaterialTextView).text.toString()
            search_bar_fragment_filter.setQuery("", false)
            search_bar_fragment_filter.clearFocus()
            addChip(fragContext, selected, chip_group_fragment_filter_added.size)
            hideKeyboard(fragContext as MainActivity)
        }
        ArrayAdapter<String>(
            fragContext,
            android.R.layout.simple_dropdown_item_1line,
            temporaryHardcodedListOfTags
        ).also {
            searchAutoComplete.setAdapter(it)
        }

        // Activate the Apply button, in effect to retain, saving selections
        button_fragment_filter_apply.setOnClickListener {
            // Save current selections in the ViewModel to be retrieved later if necessary
            val filter: Filter = Filter()
            filter.location = spinner_fragment_filter_location.selectedItemId.toInt()
            filter.zip = edit_text_fragment_filter_zip_code.text.toString()
            filter.date = text_view_fragment_filter_date_shown.text.toString()
            filter.tags = getSelectedTags()
            sharedFilterViewModel.setSharedData(filter)

            // Dismiss the soft keyboard if it is showing
            hideKeyboard(fragContext as MainActivity)
            // Dismiss this fragment, after having saved any user selections
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun addChip(fragContext: Context, tagText: String, index: Int) {
        val chip: Chip = ViewUtil.generateChip(fragContext, true)
        chip.text = tagText
        chip.id = index
        chip.setOnCloseIconClickListener {
            chip_group_fragment_filter_added.removeView(it)
        }
        chip_group_fragment_filter_added.addView(chip)
    }

    // Build up a list of strings representing each Chip tag
    private fun getSelectedTags(): List<String> {
        val tags: MutableList<String> = mutableListOf<String>()
        chip_group_fragment_filter_added.forEach {
            tags.add((it as Chip).text.toString())
        }

        return tags
    }

    // Change the values of the views to match the values in the Filter object
    private fun setUpSelections(filter: Filter) {
        if (filter.location < 0)
            spinner_fragment_filter_location.setSelection(0)
        else
            spinner_fragment_filter_location.setSelection(filter.location)

        if (filter.zip.isBlank())
            edit_text_fragment_filter_zip_code.setText("")
        else
            edit_text_fragment_filter_zip_code.setText(filter.zip)

        if (filter.date.isBlank())
            text_view_fragment_filter_date_shown.text = ""
        else
            text_view_fragment_filter_date_shown.text = filter.date
    }
}
