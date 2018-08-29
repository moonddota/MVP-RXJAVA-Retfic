package app.com.skylinservice.injection

import app.com.skylinservice.AppApplication
import app.com.skylinservice.injection.module.ApiModule
import app.com.skylinservice.injection.module.AppModule
import app.com.skylinservice.injection.module.DataModule
import app.com.skylinservice.injection.module.PresenterModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class, ApiModule::class))
interface AppComponent {
    operator fun plus(presenterModule: PresenterModule): ConfigPersistentComponent

    fun inject(app: AppApplication)
}
