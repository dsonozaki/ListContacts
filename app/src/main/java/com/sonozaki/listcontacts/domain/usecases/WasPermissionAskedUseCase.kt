package com.sonozaki.listcontacts.domain.usecases

import com.sonozaki.listcontacts.domain.repositories.PermissionsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WasPermissionAskedUseCase @Inject constructor(private val permissionsRepository: PermissionsRepository) {
    suspend operator fun invoke(permission: String): Boolean {
        return permissionsRepository.wasPermissionAsked(permission).first()
    }
}