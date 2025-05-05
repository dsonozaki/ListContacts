package com.sonozaki.listcontacts.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.sonozaki.listcontacts.domain.repositories.PermissionsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PermissionsRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    PermissionsRepository {

    private val Context.permissionsDatastore by preferencesDataStore(
        name = PERMISSION_PREFERENCES_NAME
    )

    override fun wasPermissionAsked(permission: String): Flow<Boolean> {
        return context.permissionsDatastore.data.map { it[booleanPreferencesKey(permission)] == true }
    }

    override suspend fun setPermissionAsked(permission: String) {
        context.permissionsDatastore.edit {
            it[booleanPreferencesKey(permission)] = true
        }
    }


    companion object {
        private const val PERMISSION_PREFERENCES_NAME = "permission_preferences"
    }
}