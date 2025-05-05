package com.sonozaki.listcontacts.di

import com.sonozaki.listcontacts.domain.entities.Contact
import com.sonozaki.listcontacts.domain.entities.DataResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ContactsModule {
    @Provides
    @Singleton
    fun provideContactsFlow(): MutableSharedFlow<DataResult<List<Contact>>> = MutableSharedFlow()
}