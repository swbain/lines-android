package com.stephenbain.lines

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@InstallIn(ApplicationComponent::class)
@Module
interface ApplicationContextModule {
    @Binds
    fun bindAppContext(@ApplicationContext context: Context) : Context
}