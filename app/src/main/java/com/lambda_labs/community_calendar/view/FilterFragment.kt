package com.lambda_labs.community_calendar.view

import EventsQuery
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.lambda_labs.community_calendar.viewmodel.FilterViewModel
import com.lambda_labs.community_calendar.viewmodel.SharedFilterViewModel
import kotlinx.android.synthetic.main.fragment_filter.*
import org.koin.android.ext.android.get
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

        // Store the context variable as an activity too
        lateinit var fragActivity: MainActivity

        // Ensure the context is not null before assigning it
        context?.let {
            fragContext = it
            fragActivity = it as MainActivity
        } ?: run {
            Log.e(FilterFragment::class.java.simpleName, "Failed to assign context!")
        }

        // Instantiate this fragment's ViewModel to gain access to the data from the repository
        val filterViewModel: FilterViewModel = get()

        // Instantiate the shared ViewModel to allow user selections to persist after fragment destruction
        val sharedFilterViewModel: SharedFilterViewModel =
            ViewModelProviders.of(fragActivity)[SharedFilterViewModel::class.java]

        // Populate the initial chip tags to be added to the included group
        sharedFilterViewModel.getSharedData().value?.tags?.forEachIndexed { index, tagText ->
            addChip(fragContext, tagText, index)
        }

        // Retrieve the stored data in the shared ViewModel and show the selections in the views
        sharedFilterViewModel.getSharedData().observe(
            viewLifecycleOwner, Observer<Filter> { filter ->
                setUpSelections(filter)
            })

        // The Set collections to store all of the unique tags and locations
        val allTags: MutableSet<String> = mutableSetOf<String>()
        val allLocations: MutableSet<String> = mutableSetOf<String>("")

        // Enable touch event on this fragment in order to prevent views beneath from responding
        view.setOnTouchListener { v, event ->
            return@setOnTouchListener true
        }

        // Reference attribute in Manifest to have proper behavior of this layout when keyboard shows
        fragActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Activate the image of the X in the upper left, in effect to cancel, discarding changes
        image_view_fragment_filter_cancel.setOnClickListener {
            // Dismiss this fragment, ignoring any user selections
            Navigation.findNavController(it).popBackStack()

            // Dismiss the soft keyboard if it is showing
            hideKeyboard(fragActivity)
        }

        // Activate the Apply button, in effect to retain, saving selections
        button_fragment_filter_apply.setOnClickListener {
            // Save current selections in the ViewModel to be retrieved later if necessary
            val filter: Filter = Filter()
            spinner_fragment_filter_location.selectedItem?.let {
                filter.location = spinner_fragment_filter_location.selectedItem.toString()
            }
            filter.zip = edit_text_fragment_filter_zip_code.text.toString()
            filter.date = text_view_fragment_filter_date_shown.text.toString()
            filter.tags = getSelectedTags()
            sharedFilterViewModel.setSharedData(filter)

            // Dismiss the soft keyboard if it is showing
            hideKeyboard(fragActivity)

            // Dismiss this fragment, after having saved any user selections
            Navigation.findNavController(it).popBackStack()
        }

        // Show the Date Picker when the date control is engaged
        image_view_fragment_filter_date.setOnClickListener {
            // Grab the displayed date from the TextView, if it exists
            var selectedDate: String = text_view_fragment_filter_date_shown.text.toString()

            // Ensure the date string has a valid value before passing it to the DatePicker
            if (selectedDate.isBlank()) selectedDate = getSearchDate(getToday())

            // Convert the string value into an actual Date object
            val date: Date = searchStringToDate(selectedDate)

            // Instantiate the DatePicker DialogFragment, passing in the date and a unique tag name
            val datePicker = DatePickerFragment(fragContext, toCalendar(date))
            datePicker.show(childFragmentManager, "datePicker")
        }

        // Set up the ArrayAdapter and assign it to the Spinner which holds the locations
        val arrayAdapterLocationSpinner: ArrayAdapter<String> = ArrayAdapter(
            fragContext,
            android.R.layout.simple_spinner_item,
            allLocations.toMutableList()
        ).also {
            it.setNotifyOnChange(true)
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_fragment_filter_location.adapter = it
        }

        // Populate the SearchView suggestions with a list of tags and when selected added as a Chip
        val searchAutoComplete =
            search_bar_fragment_filter.findViewById<View>(androidx.appcompat.R.id.search_src_text) as SearchAutoComplete
        searchAutoComplete.dropDownAnchor = R.id.search_bar_fragment_filter
        searchAutoComplete.threshold = 1
        searchAutoComplete.setOnItemClickListener { parent, view1, position, id ->
            val selected: String = (view1 as MaterialTextView).text.toString()
            search_bar_fragment_filter.setQuery("", false)
            search_bar_fragment_filter.clearFocus()
            addChip(fragContext, selected, chip_group_fragment_filter_added.size)
            hideKeyboard(fragActivity)
        }

        // Set up the ArrayAdapter and assign it to the SearchView which holds tags
        val arrayAdapterTagSearchView: ArrayAdapter<String> = ArrayAdapter<String>(
            fragContext,
            android.R.layout.simple_dropdown_item_1line,
            allTags.toMutableList()
        ).also {
            it.setNotifyOnChange(true)
            searchAutoComplete.setAdapter(it)
        }

        // Access LiveData via the ViewModel to get the Events data stored in the repository
        filterViewModel.getAllEvents().observe(this, Observer<List<EventsQuery.Event>> { events ->
            events.forEach { event ->
                // Populate the list of tags from each Event
                event.tags()?.forEach { tag -> allTags.add(tag.title()) }

                // Populate the list of locations from each Event
                event.locations()?.forEach { location -> allLocations.add(location.name()) }
            }

            // TODO: Eliminate this block once ViewModel's event list is working
            /*allTags.addAll(
                listOf<String>(
                    "Tech",
                    "Entertainment",
                    "Gardening",
                    "Sewing",
                    "Sports",
                    "Outdoors",
                    "Music",
                    "Family",
                    "Fun",
                    "Eating",
                    "Cooking"
                )
            )*/

            // TODO: Eliminate this block once ViewModel's event list is working
            /*allLocations.addAll(
                listOf<String>(
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
            )*/

            // Refresh the array adapters/chips with data retrieved from the repository
            arrayAdapterTagSearchView.addAll(allTags)
            arrayAdapterLocationSpinner.remove("")
            arrayAdapterLocationSpinner.addAll(allLocations)

            // Randomize (shuffle) a maximum of 10 tags to be displayed
            addSuggestedChips(allTags, fragContext)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    // Create Chips with random tag names and then put them into the 'suggested' ChipGroup
    private fun addSuggestedChips(allTags: MutableSet<String>, context: Context) {
        val shuffledTags = allTags.toMutableList()
        shuffledTags.shuffle()
        var randomTagCount = shuffledTags.size
        if (shuffledTags.size > 10) randomTagCount = 10
        for (x in 0 until randomTagCount) {
            val chip: Chip = ViewUtil.generateChip(context, false)
            chip.text = shuffledTags[x]
            chip.id = x
            chip.setOnCloseIconClickListener {
                chip_group_fragment_filter_suggested.removeView(it)
                val chipChange: Chip = ViewUtil.generateChip(context, true)
                chipChange.text = (it as Chip).text
                chipChange.id = chip_group_fragment_filter_added.childCount + 1
                chipChange.setOnCloseIconClickListener {
                    chip_group_fragment_filter_added.removeView(chipChange)
                }
                chip_group_fragment_filter_added.addView(chipChange)
            }
            chip_group_fragment_filter_suggested.addView(chip)
        }
    }

    // Create a Chip and put it into the 'added' ChipGroup
    private fun addChip(context: Context, tagText: String, index: Int) {
        val chip: Chip = ViewUtil.generateChip(context, true)
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

    // Change what is displayed in each view to match the values in the passed object
    private fun setUpSelections(filter: Filter) {
        if (filter.location.isBlank())
            spinner_fragment_filter_location.setSelection(0)
        else
            spinner_fragment_filter_location.setSelection(
                getSpinnerItemPosition(spinner_fragment_filter_location, filter.location)
            )

        if (filter.zip.isBlank())
            edit_text_fragment_filter_zip_code.setText("")
        else
            edit_text_fragment_filter_zip_code.setText(filter.zip)

        if (filter.date.isBlank())
            text_view_fragment_filter_date_shown.text = ""
        else
            text_view_fragment_filter_date_shown.text = filter.date
    }

    // Find the index of the Spinner item which matches the passed in text
    private fun getSpinnerItemPosition(spinner: Spinner, itemText: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(itemText))
                return i
        }

        return 0
    }
}
