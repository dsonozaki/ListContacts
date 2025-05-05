package com.sonozaki.listcontacts.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PermissionsRepository {
    fun wasPermissionAsked(permission: String): Flow<Boolean>
    suspend fun setPermissionAsked(permission: String)
}