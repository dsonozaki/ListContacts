package com.sonozaki.listcontacts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.sonozaki.listcontacts.R
import com.sonozaki.listcontacts.ui.state.ContactsUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ErrorComponent(state: ContactsUiState.Error, reloadContacts: () -> Unit, permissions: MultiplePermissionsState) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(dimensionResource(R.dimen.big_padding), 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = state.message.asString(context),
            maxLines = 3,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
        Button(onClick = {
            //reload contacts if all permissions were granted
            if (permissions.allPermissionsGranted) {
                reloadContacts()
            }
        }) {
            Text(text = stringResource(R.string.reload))
        }
    }
}