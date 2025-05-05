package com.sonozaki.listcontacts.ui.viewmodel

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_CONTACTS
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonozaki.listcontacts.STOP_TIMEOUT_MILLIS
import com.sonozaki.listcontacts.domain.entities.Contact
import com.sonozaki.listcontacts.domain.entities.DataResult
import com.sonozaki.listcontacts.domain.usecases.GetContactsUseCase
import com.sonozaki.listcontacts.domain.usecases.ReloadContactsUseCase
import com.sonozaki.listcontacts.domain.usecases.SetPermissionAskedUseCase
import com.sonozaki.listcontacts.domain.usecases.WasPermissionAskedUseCase
import com.sonozaki.listcontacts.ui.state.ContactsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    getContactsUseCase: GetContactsUseCase,
    private val reloadContactsUseCase: ReloadContactsUseCase,
    private val contactsUiStateFlow: MutableSharedFlow<ContactsUiState>,
    private val setPermissionAskedUseCase: SetPermissionAskedUseCase,
    private val wasPermissionAskedUseCase: WasPermissionAskedUseCase,
) : ViewModel() {


    val contactsState = merge(getContactsUseCase().map {
        when (it) {
            is DataResult.Data<List<Contact>> -> ContactsUiState.Success(it.data.groupBy { it.name[0] })
            is DataResult.Error<*> -> ContactsUiState.Error(it.errorDescription)
        }
    }, contactsUiStateFlow).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        ContactsUiState.Loading
    )

    suspend fun wasPermissionAsked(permission: String): Boolean {
        return wasPermissionAskedUseCase(permission)
    }

    fun reloadContacts() {
        viewModelScope.launch {
            reloadContactsUseCase()
        }
    }

    fun handlePermissionWasDenied() {
        viewModelScope.launch {
            contactsUiStateFlow.emit(ContactsUiState.NoPermission)
        }
    }
}