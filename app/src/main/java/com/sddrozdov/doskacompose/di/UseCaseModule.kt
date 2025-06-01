package com.sddrozdov.doskacompose.di

import com.sddrozdov.doskacompose.domain.repository.AdRepository
import com.sddrozdov.doskacompose.domain.repository.AuthRepository
import com.sddrozdov.doskacompose.domain.useCase.AdUseCase
import com.sddrozdov.doskacompose.domain.useCase.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideAdUseCase(repository: AdRepository): AdUseCase {
        return AdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(repository: AuthRepository): AuthUseCase {
        return AuthUseCase(repository)
    }
}