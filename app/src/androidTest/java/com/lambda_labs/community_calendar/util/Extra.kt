package com.lambda_labs.community_calendar.util

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry

object Extra {

    fun get_resource_id_from_string(idString: String): Int {
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().context
        val packageName: String = targetContext.packageName
        return targetContext.resources.getIdentifier(idString, "id", packageName)
    }
}