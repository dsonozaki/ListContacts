package com.sonozaki.listcontacts.domain.usecases

import com.sonozaki.listcontacts.domain.entities.Contact
import com.sonozaki.listcontacts.domain.entities.DataResult
import com.sonozaki.listcontacts.domain.repositories.ContactsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val repository: ContactsRepository) {
    operator fun invoke(): Flow<DataResult<List<Contact>>> = repository.contactsFlow
}