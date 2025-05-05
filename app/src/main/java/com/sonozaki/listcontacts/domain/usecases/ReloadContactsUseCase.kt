package com.sonozaki.listcontacts.domain.usecases

import com.sonozaki.listcontacts.domain.repositories.ContactsRepository
import javax.inject.Inject

class ReloadContactsUseCase @Inject constructor(private val repository: ContactsRepository) {
    suspend operator fun invoke() {
        repository.reloadContacts()
    }
}