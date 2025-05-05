package com.sonozaki.listcontacts.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sonozaki.listcontacts.R
import com.sonozaki.listcontacts.ui.utils.launchAppsSettings

@Composable
fun ContactsPermissionSettingsDialog(disableDialog: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            disableDialog()
        },
        title = {
            Text(
                text = stringResource(R.string.call_permission_was_denied),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Text(
                stringResource(R.string.call_permission_was_denied_long),
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    launchAppsSettings(context)
                }) {
                Text(stringResource(R.string.open_settings))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    disableDialog()
                }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}