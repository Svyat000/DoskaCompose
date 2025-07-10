package com.sddrozdov.doskacompose.di

import Appwrt.ENDPOINT
import Appwrt.PROJECT_ID
import android.content.Context
import dagger.Module


import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppwriteModule {

    @Provides
    @Singleton
    fun provideAppwriteClient(@ApplicationContext context: Context): Client {
        return Client(context)
            .setEndpoint(ENDPOINT)
            .setProject(PROJECT_ID)
            //.setSelfSigned(true)
    }

    @Provides
    @Singleton
    fun provideStorage(client: Client): Storage {
        return Storage(client)
    }

}