package app.com.skylinservice.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import app.com.skylinservice.injection.ActivityComponent
import app.com.skylinservice.injection.module.ActivityModule

open class BaseFragment : Fragment() {

    val configPersistDelegate = ConfigPersistentDelegate()
    lateinit var activityComponent: ActivityComponent


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        configPersistDelegate.onSaveInstanceState(outState ?: return)
    }

    override fun onDestroy() {
        super.onDestroy()
        configPersistDelegate.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        configPersistDelegate.onCreate(activity!!, savedInstanceState)
        activityComponent = configPersistDelegate.component + ActivityModule(activity!!)

        super.onCreate(savedInstanceState)
    }

    companion object {
        val TITLE_KEY = "TITLE_KEY"
        val DES_KEY = "DES_KEY"
        val IMAGE_KEY = "IMAGE_KEY"

        val KEY_CLASSES = 0
        val KEY_SETS = 1
        val KEY_TYPES = 2
        val KEY_FACTIONS = 3
        val KEY_QUALITIES = 4
        val KEY_RACES = 5
    }


}