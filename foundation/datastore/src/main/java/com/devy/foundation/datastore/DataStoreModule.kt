package com.devy.foundation.datastore

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.osipxd.security.crypto.createEncrypted
import javax.inject.Singleton

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context,
    ) = PreferenceDataStoreFactory.createEncrypted {
        EncryptedFile.Builder(
            context.dataStoreFile("${Constants.DATA_STORE_FILE_NAME}.preferences_pb"),
            context,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataStore(
        dataStore: androidx.datastore.core.DataStore<Preferences>
    ) = DataStore(dataStore)

    class Constants {
        companion object {
            const val DATA_STORE_FILE_NAME = "WorkSpace"
        }
    }
}