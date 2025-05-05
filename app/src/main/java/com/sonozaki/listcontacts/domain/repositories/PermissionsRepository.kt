package com.sonozaki.listcontacts.domain.repositories

import kotlinx.coroutines.flow.Flow

/**
 * Repository to store information about whether permissions have been requested at least once before. Needed to distinguish between situations where a user has permanently denied a permission and has never requested a permission before
 */
interface PermissionsRepository {
    fun wasPermissionAsked(permission: String): Flow<Boolean>
    suspend fun setPermissionAsked(permission: String)
}