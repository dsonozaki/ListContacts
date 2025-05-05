package com.sonozaki.listcontacts.di

import com.sonozaki.listcontacts.ui.state.ContactsUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelsModule {
    @Provides
    @ViewModelScoped
    fun provideContactsUIFlow(): MutableSharedFlow<ContactsUiState> = MutableSharedFlow()
}