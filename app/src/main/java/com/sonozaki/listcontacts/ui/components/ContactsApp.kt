package com.sonozaki.listcontacts.ui.components

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.sonozaki.listcontacts.domain.entities.Contact
import com.sonozaki.listcontacts.domain.usecases.WasPermissionAskedUseCase
import com.sonozaki.listcontacts.ui.state.ContactsUiState
import com.sonozaki.listcontacts.ui.viewmodel.ContactsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactsApp(modifier: Modifier = Modifier) {
    val viewModel: ContactsViewModel = hiltViewModel()
    val uiState = viewModel.contactsState.collectAsStateWithLifecycle()
    val reloadContacts = remember { { viewModel.reloadContacts() } }
    val contactsPermission = rememberPermissionState(
        permission = READ_CONTACTS,
        onPermissionResult = { results: Boolean ->
            if (results) {
                reloadContacts()
            } else {
                viewModel.handlePermissionWasDenied()
            }
        }
    )
    val showCallPermissionDialog = remember { mutableStateOf(false) }
    val disablePermissionDialog = remember { { showCallPermissionDialog.value = false } }
    val callsPermission = rememberPermissionState(
        permission = CALL_PHONE,
        onPermissionResult = { results: Boolean ->
            if (!results) {
                showCallPermissionDialog.value = true
            }
        }
    )
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        launch {
            if (contactsPermission.status.isGranted) {
                reloadContacts()
            } else {
                if (contactsPermission.status.shouldShowRationale) {
                    Log.w("rationale","true")
                    viewModel.handlePermissionWasDenied()
                } else {
                    if (viewModel.wasPermissionAsked(READ_CONTACTS)) {
                        viewModel.handlePermissionWasDenied()
                    } else {
                        contactsPermission.launchPermissionRequest()
                    }
                }
            }
        }
    }

    if (showCallPermissionDialog.value) {
        ContactsPermissionSettingsDialog(disablePermissionDialog)
    }

    Box(modifier.fillMaxSize()) {
        when (val state = uiState.value) {
            is ContactsUiState.Loading ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            is ContactsUiState.Error ->
                ErrorComponent(state, reloadContacts)

            is ContactsUiState.Success ->
                ContactsComponent(state.contacts) { contact: Contact ->
                    coroutineScope.launch {
                        handlePhoneCall(
                            callsPermission,
                            contact,
                            context,
                            { viewModel.wasPermissionAsked(it) },
                            showCallPermissionDialog
                        )
                    }
                }

            is ContactsUiState.NoPermission ->
                PermissionDeniedComponent()
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
private suspend fun handlePhoneCall(
    callsPermission: PermissionState,
    contact: Contact,
    context: Context,
    wasPermissionAsked: suspend (String) -> Boolean,
    showCallPermissionDialog: MutableState<Boolean>
) {
    if (callsPermission.status.isGranted) {
        if (contact.phone != null) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = "tel:${contact.phone}".toUri()
            }
            context.startActivity(intent)
        }
    } else {
        if (wasPermissionAsked(CALL_PHONE)) {
            showCallPermissionDialog.value = true
        } else {
            callsPermission.launchPermissionRequest()
        }
    }
}