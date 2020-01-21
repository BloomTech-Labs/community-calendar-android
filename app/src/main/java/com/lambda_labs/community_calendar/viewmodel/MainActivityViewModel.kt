package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.fonts.FontFamily
import android.text.SpannableString
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.button.MaterialButton
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.Repository
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.util.hideKeyboard
import com.lambda_labs.community_calendar.util.searchEvents
import com.lambda_labs.community_calendar.view.MainActivity
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(val repo: Repository) : ViewModel() {

    // Creates LiveData to be observed on the HomeFragment


    // Init a global variable to be able to call it from another function
    private var disposable: Disposable? = null
    private var searchDisposable: Disposable? = null



    // For MainActivity, add a Recent Search to room's database
    fun addRecentSearch(search: Search){
        searchDisposable = Schedulers.io().createWorker().schedule {
            repo.addRecentSearch(search)
        }
    }

    fun queryEvents(){
        disposable = repo.getEvents()
    }

    fun getAllEvents(): LiveData<List<EventsQuery.Event>> {
        return repo.events
    }

    // Search actions
    fun searchNSave(searchView: SearchView, events: ArrayList<EventsQuery.Event>){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    // Function connects to repository (see above function)
                    val userSearch = Search(query)
                    searchEvents(events, userSearch).forEach {
                        println(it.title())
                    }
                    addRecentSearch(userSearch)

                }
                hideKeyboard(searchView.context as MainActivity)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    // Disposes of the disposable to prevent memory leak
    override fun onCleared() {
        if (disposable != null){
            disposable?.dispose()
        }
        if (searchDisposable != null){
            searchDisposable?.dispose()
        }
        super.onCleared()
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
    fun setSearchBarProperties(searchView: SearchView){
        val width = LinearLayoutCompat.LayoutParams.MATCH_PARENT
        val height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        val layoutParams = LinearLayoutCompat.LayoutParams(width, height)
        layoutParams.setMargins(16)
        searchView.layoutParams = layoutParams
        searchView.background = ContextCompat.getDrawable(searchView.context, R.drawable.rectangle)
        searchView.queryHint = searchView.context.resources.getString(R.string.search_hint)
        searchView.setIconifiedByDefault(false)
        val image = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        image.layoutParams = LinearLayout.LayoutParams(0, 0)
        searchView.id = View.generateViewId()
    }

    fun setCancelButtonPropetioes(materialButton: MaterialButton){
        val width = LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        val height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        val layoutParams = LinearLayoutCompat.LayoutParams(width, height)
        layoutParams.setMargins(0, 0, 16, 0)
        materialButton.layoutParams = layoutParams
        // requires API lvl 26 materialButton.context.resources.getFont(R.font.poppins_regular)
        val font: Typeface = Typeface.createFromAsset(materialButton.context.assets, "poppins_regular.ttf")
        materialButton.typeface = font
        materialButton.text = "Cancel"
        materialButton.textSize = 14f
        materialButton.setSupportAllCaps(false)
        materialButton.setBackgroundColor(ContextCompat.getColor(materialButton.context, android.R.color.background_light))
        materialButton.setTextColor(Color.parseColor("#66000000"))
        materialButton.id = View.generateViewId()
    }
    /*
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btn_cancel"
        android:visibility="gone"
        style="@style/Widget.MaterialComponents.Button.TextButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="@font/poppins_regular"
        android:text="Cancel"
        android:textSize="14sp"
        android:textAllCaps="false"
        android:textColor="#66000000"
        app:layout_constraintBottom_toBottomOf="@id/search_bar"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintLeft_toRightOf="@id/search_bar"
        app:layout_constraintTop_toTopOf="@id/search_bar" />
    */
}