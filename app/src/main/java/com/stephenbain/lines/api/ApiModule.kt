package com.stephenbain.lines.api

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
    fun provideLinesApiService(moshi: Moshi): LinesApiService {

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(TopicJsonAdapter()).build()
    }
}

const val BASE_URL = "https://llllllll.co"