package com.lambda_labs.community_calendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Timestamp

@Entity
data class RecentSearch(val searchText: String, @PrimaryKey(autoGenerate = true)val id: Int = 0/*, val timestamp: Timestamp*/)