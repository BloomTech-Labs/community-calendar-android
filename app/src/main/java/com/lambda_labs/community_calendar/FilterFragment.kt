package com.lambda_labs.community_calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : Fragment()/*, AdapterView.OnItemSelectedListener*/ {

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
            /*spinner_fragment_filter_location.onItemSelectedListener = this*/
        }
    }

    /*override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}*/
}
