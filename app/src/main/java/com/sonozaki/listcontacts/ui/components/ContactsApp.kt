package com.sonozaki.listcontacts.ui.components

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_CONTACTS
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sonozaki.listcontacts.ui.state.ContactsUiState
import com.sonozaki.listcontacts.ui.viewmodel.ContactsViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactsApp(modifier: Modifier = Modifier) {
    val viewModel: ContactsViewModel = hiltViewModel()
    val uiState = viewModel.contactsState.collectAsStateWithLifecycle()
    val permissions = rememberMultiplePermissionsState(
        permissions= listOf(READ_CONTACTS, CALL_PHONE),
        onPermissionsResult= { results: Map<String, Boolean> ->
            if (results.all { it.value }) {
                //load contacts when all permissions granted
                viewModel.reloadContacts()
            } else {
                viewModel.handlePermissionWasDenied()
            }
        }
    )

    LaunchedEffect(uiState.value) {
        if (uiState.value !is ContactsUiState.Loading) {
            return@LaunchedEffect
        }
        if (permissions.allPermissionsGranted) {
            //load contacts when all permissions granted
            viewModel.reloadContacts()
        } else {
            if (viewModel.wasPermissionAsked(READ_CONTACTS) || viewModel.wasPermissionAsked(CALL_PHONE)) {
                //ask user to open settings if some of the permissions were denied previously
                viewModel.handlePermissionWasDenied()
            } else {
                //request all permissions if user never asked permissions before
                permissions.launchMultiplePermissionRequest()
                viewModel.setPermissionAsked(READ_CONTACTS)
                viewModel.setPermissionAsked(CALL_PHONE)
            }
        }
    }

    Box(modifier.fillMaxSize()) {
        when (val state = uiState.value) {
            is ContactsUiState.Loading ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            is ContactsUiState.Error ->
                ErrorComponent(state, viewModel::reloadContacts, permissions)

            is ContactsUiState.Success -> {
                ContactsComponent(state.contacts, permissions)
            }
            is ContactsUiState.NoPermission -> {
                PermissionDeniedComponent(viewModel::reloadContacts, permissions)
            }
        }
    }
}