package com.sonozaki.listcontacts.domain.usecases

import com.sonozaki.listcontacts.domain.repositories.PermissionsRepository
import javax.inject.Inject

class SetPermissionAskedUseCase @Inject constructor(private val permissionsRepository: PermissionsRepository) {
    suspend operator fun invoke(permission: String) {
        permissionsRepository.setPermissionAsked(permission)
    }
}