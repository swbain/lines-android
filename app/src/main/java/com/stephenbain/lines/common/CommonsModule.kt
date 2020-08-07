package com.stephenbain.lines.common

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object CommonsModule {
    @Provides
    fun providePicasso(): Picasso {
        return Picasso.get()
    }
}