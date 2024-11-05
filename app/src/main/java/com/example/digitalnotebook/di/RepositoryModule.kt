package com.example.digitalnotebook.di

import com.example.digitalnotebook.db.repositories.TaskRepository
import com.example.digitalnotebook.db.repositories.TaskRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        firebaseFirestore: FirebaseFirestore,
        auth: FirebaseAuth,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ):TaskRepository{
        return TaskRepositoryImpl(
            firestore = firebaseFirestore,
            auth = auth,
            ioDispatcher = ioDispatcher,

        )
    }
}