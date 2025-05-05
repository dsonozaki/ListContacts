package com.sonozaki.listcontacts.domain.entities

import android.net.Uri

data class Contact(val name: String, val phone: String?, val contactUri: Uri)