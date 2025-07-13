package com.sddrozdov.doskacompose.di

import com.sddrozdov.domain.useCase.AuthUseCase
import com.sddrozdov.domain.useCase.AuthUseCaseInterface
import com.sddrozdov.domain.useCase.CreateAdUseCase
import com.sddrozdov.domain.useCase.CreateAdUseCaseInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindCreateAdUseCase(
        impl: CreateAdUseCase
    ): CreateAdUseCaseInterface

    @Binds
    @Singleton
    abstract fun bindCreateAuthUseCase(
        impl: AuthUseCase
    ): AuthUseCaseInterface


}