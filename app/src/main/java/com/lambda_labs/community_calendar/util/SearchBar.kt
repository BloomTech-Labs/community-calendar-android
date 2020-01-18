package com.lambda_labs.community_calendar.util

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

// TODO: these will be things required in searchBar object, need DI
// fun addSearchBarToLayout(){}
// cancel Button Is Only Going To Be Inside Of Search Fragment And ResultsFragment(){}

class SearchBar(context: Context): SearchView(context) {
    //        <androidx.appcompat.widget.SearchView
//        android:id="@+id/search_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_margin="10dp"
//        android:background="@drawable/rectangle"
//        android:hint="@string/search_hint"
//        app:iconifiedByDefault="false"
//        app:queryBackground="@android:color/transparent"
//        app:queryHint="@string/search_hint"
//        app:searchHintIcon="@null"
//        app:searchIcon="@null"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toTopOf="parent"
//        app:layout_constraintBottom_toTopOf="@id/nav_host_fragment"/>

    init {
        val layoutWidth = ViewGroup.LayoutParams.WRAP_CONTENT
        val layoutHeight = ViewGroup.LayoutParams.MATCH_PARENT
        val viewLayoutParams = ViewGroup.LayoutParams(layoutWidth, layoutHeight)

        this.layoutParams = viewLayoutParams
    }
}