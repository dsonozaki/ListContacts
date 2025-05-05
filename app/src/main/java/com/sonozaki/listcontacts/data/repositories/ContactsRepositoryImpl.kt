package com.sonozaki.listcontacts.data.repositories

import android.content.ContentUris
import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import com.sonozaki.listcontacts.R
import com.sonozaki.listcontacts.core.ui.UIText
import com.sonozaki.listcontacts.domain.entities.Contact
import com.sonozaki.listcontacts.domain.entities.DataResult
import com.sonozaki.listcontacts.domain.repositories.ContactsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
    private val _contactsFlow: MutableSharedFlow<DataResult<List<Contact>>>
) :
    ContactsRepository {
    override val contactsFlow: Flow<DataResult<List<Contact>>>
        get() = _contactsFlow.asSharedFlow()

    /**
     * Helper method to get a phone number for a specific contact ID
     */
    private fun getPhoneNumber(id: Long): String? {
        val phoneCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // URI for phone data
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER), // Only fetch phone number
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", // Filter by contact ID
            arrayOf(id.toString()), // Selection args
            null // No sort order
        )
        return phoneCursor?.use {
            val phoneIndex =
                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (phoneCursor.moveToNext()) {
                return@use phoneCursor.getString(phoneIndex)
            } else {
                return@use null
            }
        }
    }

    /**
     * Main method to reload contacts from the system into the flow
     */
    override suspend fun reloadContacts() {
        withContext(ioDispatcher) {
            val list = mutableListOf<Contact>()
            val resolver = context.contentResolver
            try {
                // Query for contact name, phone availability, and ID
                val cursor = resolver.query(
                    Contacts.CONTENT_URI,
                    arrayOf(
                        Contacts.DISPLAY_NAME,
                        Contacts.HAS_PHONE_NUMBER,
                        Contacts._ID
                    ),
                    null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC" // Sort by name
                )
                cursor?.use {
                    val hasPhoneNumberIndex = cursor.getColumnIndex(Contacts.HAS_PHONE_NUMBER)
                    val nameIndex =
                        it.getColumnIndex(Contacts.DISPLAY_NAME)
                    val id = it.getColumnIndex(Contacts._ID)

                    while (it.moveToNext()) {
                        val hasPhoneNumber = it.getInt(hasPhoneNumberIndex) > 0
                        val name = it.getString(nameIndex)
                        val id = it.getLong(id)
                        // If contact has phone number, fetch it
                        val phone = if (hasPhoneNumber) {
                            getPhoneNumber(id)
                        } else {
                            null
                        }
                        // Construct contact URI for getting photo
                        val contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id)
                        list.add(Contact(name, phone, contactUri))
                    }
                }
                _contactsFlow.emit(DataResult.Data<List<Contact>>(list))
            } catch (e: Exception) {
                _contactsFlow.emit(
                    DataResult.Error(
                        UIText(
                            R.string.error_while_loading_contacts,
                            e.message ?: ""
                        )
                    )
                )
            }
        }
    }
}