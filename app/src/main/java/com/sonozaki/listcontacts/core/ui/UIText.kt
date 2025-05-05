package com.sonozaki.listcontacts.core.ui

import android.content.Context

/**
 * Strings value holder
 */
class UIText(val id: Int, vararg val args: Any) {
    fun asString(context: Context): String {
        return context.getString(id, *args)
    }
}