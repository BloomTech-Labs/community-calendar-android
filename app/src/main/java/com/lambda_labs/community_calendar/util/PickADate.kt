package com.lambda_labs.community_calendar.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_filter.*
import java.util.*

class DatePickerFragment(val dialogContext: Context, val cal: Calendar) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Split the Calendar object into its time components
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(dialogContext, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Show the date in the standard way in the text view within the parent
        parentFragment?.text_view_fragment_filter_date_shown?.text =
            parseDate(year, month, day)
    }
}