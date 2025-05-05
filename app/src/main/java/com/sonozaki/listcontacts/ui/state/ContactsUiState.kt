package com.sonozaki.listcontacts.ui.state

import com.sonozaki.listcontacts.core.ui.UIText
import com.sonozaki.listcontacts.domain.entities.Contact

sealed class ContactsUiState {
    data object Loading : ContactsUiState()
    data class Success(val contacts: Map<Char, List<Contact>>) : ContactsUiState()
    data class Error(val message: UIText) : ContactsUiState()
    data object NoPermission : ContactsUiState()
}