package com.sonozaki.listcontacts.domain.entities

import com.sonozaki.listcontacts.core.ui.UIText

/**
 * Class for results representation
 */
sealed class DataResult<T> {
    data class Data<T>(val data: T) : DataResult<T>()
    data class Error<T>(val errorDescription: UIText) : DataResult<T>()
}