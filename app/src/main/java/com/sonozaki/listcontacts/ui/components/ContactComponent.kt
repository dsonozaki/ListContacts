package com.sonozaki.listcontacts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.sonozaki.listcontacts.domain.entities.Contact

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactsComponent(
    sortedContacts: Map<Char, List<Contact>>,
    handleCall: (Contact) -> Unit
) {
    LazyColumn {
        sortedContacts.forEach { (letter, contacts) ->
            stickyHeader {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    Text(text = letter.toString(), fontWeight = FontWeight.Bold)
                }
            }

            items(contacts, { it.contactUri }) { contact ->
                ContactItem(contact = contact) {
                    handleCall(contact)
                }
            }
        }
    }
}
