package com.dshelper.app.di

import com.dshelper.app.data.repository.AuthRepositoryImpl
import com.dshelper.app.data.repository.PostRepositoryImpl
import com.dshelper.app.data.repository.ReservationRepositoryImpl
import com.dshelper.app.domain.repository.AuthRepository
import com.dshelper.app.domain.repository.PostRepository
import com.dshelper.app.domain.repository.ReservationRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindReservationRepository(
        reservationRepositoryImpl: ReservationRepositoryImpl
    ): ReservationRepository
    
}
