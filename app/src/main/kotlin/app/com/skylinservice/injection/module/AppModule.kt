package app.com.skylinservice.injection.module

import android.content.Context
import app.com.skylinservice.AppApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: AppApplication) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): AppApplication = application

    @Provides
    @Singleton
    fun providesGson(): Gson =
            GsonBuilder()
                    .serializeNulls()
                    .create()
}