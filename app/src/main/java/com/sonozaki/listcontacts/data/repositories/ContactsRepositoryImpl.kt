package com.sonozaki.listcontacts.data.repositories

import android.content.ContentUris
import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.util.Log
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
import com.sonozaki.listcontacts.R

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
    private val _contactsFlow: MutableSharedFlow<DataResult<List<Contact>>>
) :
    ContactsRepository {
    override val contactsFlow: Flow<DataResult<List<Contact>>>
        get() = _contactsFlow.asSharedFlow()

    private fun getPhoneNumber(id: Long): String? {
        val phoneCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(id.toString()),
            null)
        return phoneCursor?.use {
            val phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (phoneCursor.moveToNext()) {
                return@use phoneCursor.getString(phoneIndex)
            } else {
                return@use null
            }
    }
    }

    override suspend fun reloadContacts() {
        withContext(ioDispatcher) {
            val list = mutableListOf<Contact>()
            val resolver = context.contentResolver
            try {
                val cursor = resolver.query(
                    Contacts.CONTENT_URI,
                    arrayOf(
                        Contacts.DISPLAY_NAME,
                        Contacts.HAS_PHONE_NUMBER,
                        Contacts._ID
                    ),
                    null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
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
                        val phone =  if (hasPhoneNumber) {
                            getPhoneNumber(id) ?: continue
                        } else {
                            null
                        }
                        val contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id)
                        list.add(Contact(name, phone, contactUri))
                    }
                }
                _contactsFlow.emit(DataResult.Data<List<Contact>>(list))
            } catch (e: Exception) {
                _contactsFlow.emit(DataResult.Error(UIText(R.string.error_while_loading_contacts, e.message ?: "")))
            }
        }
    }
}