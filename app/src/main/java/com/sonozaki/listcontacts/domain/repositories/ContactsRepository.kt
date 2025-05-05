package com.sonozaki.listcontacts.domain.repositories

import com.sonozaki.listcontacts.domain.entities.Contact
import com.sonozaki.listcontacts.domain.entities.DataResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository for contacts on device
 */
interface ContactsRepository {
    val contactsFlow: Flow<DataResult<List<Contact>>>
    suspend fun reloadContacts()
}