package com.sonozaki.listcontacts.ui.components

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.sonozaki.listcontacts.R
import com.sonozaki.listcontacts.domain.entities.Contact

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactsComponent(
    sortedContacts: Map<Char, List<Contact>>,
    permissions: MultiplePermissionsState
) {
    LazyColumn {
        sortedContacts.forEach { (letter, contacts) ->
            //header with first alphabet letter
            stickyHeader {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.normal_padding))
                ) {
                    Text(text = letter.toString(), fontWeight = FontWeight.Bold)
                    HorizontalDivider()
                }
            }

            items(contacts, { it.contactUri }) { contact ->
                ContactItem(contact = contact) { context ->
                    //make a call if all permissions were granted and phone number exists
                    if (permissions.allPermissionsGranted && contact.phone != null) {
                        val intent = Intent(Intent.ACTION_CALL).apply {
                            data = "tel:${contact.phone}".toUri()
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}
