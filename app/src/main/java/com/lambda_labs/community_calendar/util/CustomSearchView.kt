package com.lambda_labs.community_calendar.util

import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.SearchView

class CustomSearchView(context: Context): SearchView(context) {
    private var listener: OnQueryTextListener? = null

    override fun setOnQueryTextListener(listener: OnQueryTextListener?) {
        super.setOnQueryTextListener(listener)
        this.listener = listener
        val searchSrcTextView = this.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        searchSrcTextView.setOnEditorActionListener { _, _, _ ->
            listener?.onQueryTextSubmit(query.toString())
            true
        }
    }
}