package app.com.skylinservice.ui

import android.content.Context
import android.os.Bundle
import app.com.skylinservice.exception.appComponent
import app.com.skylinservice.injection.module.PresenterModule
import app.com.skylinservice.injection.ConfigPersistentComponent
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

class ConfigPersistentDelegate {

    companion object {
        private const val KEY_ID = "KEY_ID"
        @JvmStatic private val NEXT_ID = AtomicLong(0)
        @JvmStatic private val componentsMap = HashMap<Long, ConfigPersistentComponent>()
    }

    private var id: Long = 0

    var instanceSaved = false
    lateinit var component: ConfigPersistentComponent
        get

    fun onCreate(context: Context, savedInstanceState: Bundle?) {
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        id = savedInstanceState?.getLong(KEY_ID) ?: NEXT_ID.getAndIncrement()

        if (componentsMap[id] != null)
            Timber.i("Reusing ConfigPersistentComponent id = $id")

        component = componentsMap.getOrPut(id, {
            Timber.i("Creating new ConfigPersistentComponent id=$id")
            context.appComponent + PresenterModule()
        })
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(KEY_ID, id)
        instanceSaved = true
    }

    fun onDestroy() {
        if (!instanceSaved) {
            Timber.i("Clearing ConfigPersistentComponent id=$id")
            componentsMap.remove(id)
        } else {
            Timber.i("Not Clearing ConfigPersistentComponent id=$id")
        }
    }
}