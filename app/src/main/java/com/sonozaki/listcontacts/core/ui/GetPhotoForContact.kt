package com.sonozaki.listcontacts.core.ui

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.Contacts
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap

fun getPhotoForContact(uri: Uri, context: Context): ImageBitmap? {
    val photoBytes = Contacts.openContactPhotoInputStream(
        context.contentResolver, uri, false)
    if (photoBytes == null) {
        return null
    }
    return photoBytes.readBytes().decodeToImageBitmap()
}