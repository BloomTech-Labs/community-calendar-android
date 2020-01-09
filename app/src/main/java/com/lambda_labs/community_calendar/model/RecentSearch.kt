package com.lambda_labs.community_calendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentSearch(val searchText: String, @PrimaryKey(autoGenerate = true)val id: Int)