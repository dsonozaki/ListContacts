package com.sonozaki.listcontacts.ui.state

import com.sonozaki.listcontacts.core.ui.UIText
import com.sonozaki.listcontacts.domain.entities.Contact
// Represents the UI state for the contacts screen
sealed class ContactsUiState {
    data object Loading : ContactsUiState()
    // Represents the state when contacts are successfully loaded
    data class Success(val contacts: Map<Char, List<Contact>>) : ContactsUiState()
    // Represents an error state with an error message
    data class Error(val message: UIText) : ContactsUiState()
    // Indicates that the app doesn't have all required permissions
    data object NoPermission : ContactsUiState()
}