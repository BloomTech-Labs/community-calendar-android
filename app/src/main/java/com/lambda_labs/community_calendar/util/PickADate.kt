package com.lambda_labs.community_calendar.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.lambda_labs.community_calendar.R
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
        parentFragment?.text_view_fragment_filter_date_shown?.setTextColor(Color.BLACK)
    }

    // Use the Cancel button on the dialog to clear the date and replace with the hint
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        parentFragment?.text_view_fragment_filter_date_shown?.text =
            getString(R.string.filter_date_hint)
        parentFragment?.text_view_fragment_filter_date_shown?.setTextColor(
            ContextCompat.getColor(
                dialogContext,
                R.color.colorHint
            )
        )
    }
}