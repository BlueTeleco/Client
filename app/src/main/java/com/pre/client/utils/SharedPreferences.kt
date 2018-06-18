package com.pre.client.utils

import android.content.Context
import com.pre.client.R

/**
 * Save the the specified key and value pair.
 */
fun shareString(context: Context, key: String, value: String) {
    val preferences = context.getSharedPreferences(context.getString(R.string.parameters_file), Context.MODE_PRIVATE)
    with(preferences.edit()) {
        putString(key, value)
        commit()
    }
}

/**
 * Read the saved value from the specified key.
 */
fun read(context: Context, key: String, default: String): String {
    return context.getSharedPreferences(context.getString(R.string.parameters_file), Context.MODE_PRIVATE)
            .getString(key, default)
}
