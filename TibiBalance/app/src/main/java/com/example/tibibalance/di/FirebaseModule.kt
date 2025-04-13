package com.example.tibibalance.di // Asegúrate que el paquete sea correcto

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore // Importa Firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt para proveer instancias de servicios de Firebase.
 */
@Module
@InstallIn(SingletonComponent::class) // Instancia única para toda la app
object FirebaseModule {

    /**
     * Provee una instancia singleton de FirebaseAuth.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    /**
     * Provee una instancia singleton de FirebaseFirestore. // <-- NUEVO
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore { // <-- NUEVO
        return FirebaseFirestore.getInstance() // <-- NUEVO
    }
}
