package com.lambda_labs.community_calendar.util

import android.app.Activity
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import com.lambda_labs.community_calendar.R
import kotlin.math.roundToInt

fun hideKeyboard(activity: Activity){
    val inputMethodManager: InputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

fun showKeyboard(activity: Activity){
    val inputMethodManager: InputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}


/*
    <androidx.appcompat.widget.SearchView
        android:id="@+id/search_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:background="@drawable/rectangle"
        android:hint="@string/search_hint"
        app:iconifiedByDefault="false"
        app:queryBackground="@android:color/transparent"
        app:queryHint="@string/search_hint"
        /app:searchHintIcon="@null"
        /app:searchIcon="@null"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/nav_host_fragment"/>
    */
fun dpToPx(dp: Float, resources: Resources): Int{
    val density = resources.displayMetrics.density
    return (dp * density).roundToInt()
    // An alternate way:
    // return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics).toInt()
}
// Creates above xml code programmatically
fun setSearchBarProperties(searchView: SearchView,
                           matchParentWidth: Boolean,
                           startMargin: Float = 13f,
                           endMargin: Float = 13f,
                           topMargin: Float = 13f,
                           bottomMargin: Float = 13f){
    val width = if (matchParentWidth){
        ConstraintLayout.LayoutParams.MATCH_PARENT
    }else{
        ConstraintLayout.LayoutParams.WRAP_CONTENT
    }
    val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
    val layoutParams = ConstraintLayout.LayoutParams(width, height)
    val r: Resources = searchView.context.resources
    layoutParams.setMargins(dpToPx(startMargin, r), dpToPx(topMargin, r), dpToPx(endMargin, r), dpToPx(bottomMargin, r))
    searchView.layoutParams = layoutParams
    searchView.background = ContextCompat.getDrawable(searchView.context, R.drawable.search_box)
    searchView.queryHint = searchView.context.resources.getString(R.string.search_hint)
    searchView.setIconifiedByDefault(false)
    val image = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
    image.layoutParams = LinearLayout.LayoutParams(0, 0)
    searchView.findViewById<View>(androidx.appcompat.R.id.search_plate).background = null
    searchView.id = View.generateViewId()
}