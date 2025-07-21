// AppModule.kt
package com.mollin.app.di

import android.content.Context
import androidx.room.Room
import com.mollin.app.data.AppDatabase
import com.mollin.app.data.user.UserDao
import com.mollin.app.data.user.UserRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app_database")
            .build()

    @Provides @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository =
        UserRepository(dao)
}
