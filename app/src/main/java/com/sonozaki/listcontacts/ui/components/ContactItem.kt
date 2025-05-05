package com.sonozaki.listcontacts.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.coroutineScope
import com.sonozaki.listcontacts.R
import com.sonozaki.listcontacts.core.ui.getPhotoForContact
import com.sonozaki.listcontacts.domain.entities.Contact
import kotlinx.coroutines.launch

@Composable
fun ContactItem(contact: Contact, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(dimensionResource(R.dimen.big_padding))
    ) {
        val icon = getPhotoForContact(contact.contactUri, context)
        val description = stringResource(R.string.contact_photo_description)
        val iconModifier = Modifier.size(dimensionResource(R.dimen.normal_icon))
        if (icon == null) {
            Icon(Icons.Default.Person, contentDescription = description, modifier = iconModifier)
        } else {
            Image(bitmap = icon, contentDescription = description, modifier = iconModifier)
        }
        Spacer(Modifier.width(dimensionResource(R.dimen.big_padding)))
        Column {
            Text(contact.name, fontWeight = FontWeight.Bold)
            Text(contact.phone ?: stringResource(R.string.phone_unknown), color = Color.Gray)
        }
    }
}