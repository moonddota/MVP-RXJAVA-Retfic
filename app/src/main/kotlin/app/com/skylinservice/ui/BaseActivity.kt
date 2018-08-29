package app.com.skylinservice.ui

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import app.com.skylinservice.AppApplication
import app.com.skylinservice.injection.ActivityComponent
import app.com.skylinservice.injection.module.ActivityModule
import app.com.skylinservice.language.MultiLanguageUtil

open class BaseActivity : AppCompatActivity() {

    val configPersistDelegate = ConfigPersistentDelegate()
    lateinit var activityComponent: ActivityComponent

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        configPersistDelegate.onSaveInstanceState(outState ?: return)
    }

    override fun onResume() {
        super.onResume()
        AppApplication.app.addActivity(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        configPersistDelegate.onDestroy()
        AppApplication.app.removeActivity(this)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configPersistDelegate.onCreate(this, savedInstanceState)
        activityComponent = configPersistDelegate.component + ActivityModule(this)


    }

    fun showSnack(parent: ViewGroup, messageResId: Int, length: Int,
                  actionLabelResId: Int? = null, action: ((View) -> Unit)? = null,
                  callback: ((Snackbar) -> Unit)? = null) {

        showSnack(parent, getString(messageResId), length, actionLabelResId?.let { getString(it) }, action, callback)
    }

    private fun showSnack(parent: ViewGroup, message: String, length: Int,
                          actionLabel: String? = null, action: ((View) -> Unit)? = null,
                          callback: ((Snackbar) -> Unit)? = null) {

        Snackbar.make(parent, message, length)
                .apply {
                    if (actionLabel != null) {
                        setAction(actionLabel, action)
                    }

                    callback?.invoke(this)
                }
                .show()
    }
}