package com.sonozaki.listcontacts.di

import com.sonozaki.listcontacts.data.repositories.ContactsRepositoryImpl
import com.sonozaki.listcontacts.data.repositories.PermissionsRepositoryImpl
import com.sonozaki.listcontacts.domain.repositories.ContactsRepository
import com.sonozaki.listcontacts.domain.repositories.PermissionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindContactsRepository(impl: ContactsRepositoryImpl): ContactsRepository

    @Binds
    @Singleton
    abstract fun bindPermissionsRepository(impl: PermissionsRepositoryImpl): PermissionsRepository
}