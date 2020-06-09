package com.stephenbain.lines

import com.stephenbain.lines.home.HomeModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, HomeModule::class]
)
interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<MyApplication>
}
