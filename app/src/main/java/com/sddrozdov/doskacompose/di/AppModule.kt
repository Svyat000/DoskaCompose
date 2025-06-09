package com.sddrozdov.doskacompose.di

import android.content.Context
import com.sddrozdov.doskacompose.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDefaultWebClientId(@ApplicationContext context: Context): String {
        return context.getString(R.string.default_web_client_id)
    }
}