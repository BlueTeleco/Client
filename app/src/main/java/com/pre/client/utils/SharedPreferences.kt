package com.pre.client.utils

import android.content.Context
import com.pre.client.R

fun shareString(context: Context, key: String, value: String) {
    val preferences = context.getSharedPreferences(context.getString(R.string.parameters_file), Context.MODE_PRIVATE)
    with(preferences.edit()) {
        putString(key, value)
        commit()
    }
}

fun read(context: Context, key: String, default: String): String {
    return context.getSharedPreferences(context.getString(R.string.parameters_file), Context.MODE_PRIVATE)
            .getString(key, default)
}
