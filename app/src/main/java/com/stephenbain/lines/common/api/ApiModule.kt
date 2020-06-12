package com.stephenbain.lines.common.api

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(ActivityComponent::class)
object ApiModule {
    @Provides
    fun provideLinesApiService(): LinesApiService {

        return Retrofit.Builder().baseUrl("https://llllllll.co")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}