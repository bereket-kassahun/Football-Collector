package com.collect22allfootball.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val PREFERENCE_MANAGER = "PREFERENCE_MANAGER"
    private val CURRENT_LEVEL = "CURRENT_LEVEL"

    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_MANAGER, MODE_PRIVATE)

    var editor: SharedPreferences.Editor = preferences.edit()


    fun setCurrentLevel(level: Int) {
        editor.putInt(CURRENT_LEVEL, level)
        editor.apply()
    }

    fun getCurrentLevel(): Int {
        return preferences.getInt(CURRENT_LEVEL, 1)
    }

}