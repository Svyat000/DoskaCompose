package com.sddrozdov.doskacompose.di

import com.sddrozdov.doskacompose.data.repository.AdRepositoryImpl
import com.sddrozdov.doskacompose.data.repository.AuthRepositoryImpl
import com.sddrozdov.doskacompose.domain.repository.AdRepository
import com.sddrozdov.doskacompose.domain.repository.AuthRepository
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
    abstract fun bindAdRepository(
        impl: AdRepositoryImpl
    ): AdRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}