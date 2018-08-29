package app.com.skylinservice

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.SoftApps
import app.com.skylinservice.injection.AppComponent
import app.com.skylinservice.injection.DaggerAppComponent
import app.com.skylinservice.injection.module.ApiModule
import app.com.skylinservice.injection.module.AppModule
import app.com.skylinservice.manager.utils.CrashHandler
import app.com.skylinservice.language.MultiLanguageUtil


import app.com.skylinservice.ui.BaseActivity
import cn.dreamtobe.filedownloader.OkHttp3Connection
import com.blankj.utilcode.util.Utils
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.util.FileDownloadHelper
import com.liulishuo.filedownloader.util.FileDownloadLog

import com.squareup.leakcanary.LeakCanary
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import org.xutils.x
import sjj.alog.Config
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import sjj.alog.Log


open class AppApplication : Application() {

    lateinit var appComponent: AppComponent

    var language = "zh-CN"

    var tmpList = mutableListOf<SoftApps>()

    private val activities = LinkedList<BaseActivity>()

    fun addActivity(activity: BaseActivity) {
        activities.remove(activity)
        activities.add(activity)
    }

    fun removeActivity(activity: BaseActivity) {
        activities.remove(activity)
    }

    fun finishAllActivity() {
        for (activity in activities) {
            activity.finish()
        }
        activities.clear()
    }

    fun getLastActivity(): BaseActivity {
        return activities.last
    }

    @Inject
    lateinit var api: Api

    companion object {

        val APP_ID = "wx05bb2dc525931d70"

        var mWxApi: IWXAPI? = null

        lateinit var app: AppApplication

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        app = this

        initLanguage()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

//        LeakCanary.install(this)

        x.Ext.init(this)

        Utils.init(this)

        buildFileDownloader()

        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, false)
        // 将该app注册到微信
        mWxApi?.registerApp(APP_ID)

        initCrashHandler()

        val config = Config()
        config.holdMultiple = false
        config.holdLev = Config.ERROR
        config.hold = true
        Config.init(config)

//
//        if (BuildConfig.DEBUG) {
//            Stetho.initialize(Stetho.newInitializerBuilder(this)
//                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                    .build())
//        }

        MultiLanguageUtil.init(this)
        //        MultiLanguageUtil.getInstance().setConfiguration();
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //        MultiLanguageUtil.getInstance().setConfiguration();
    }

    private fun initLanguage() {
        val locale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0)
        } else {
            locale = Locale.getDefault()
        }
        val s = locale.language

        if (s == "en") {
            language = "en-US"
        } else {
            language = "zh-CN"
        }
    }

    private fun buildFileDownloader() {
        buildComponent()
        FileDownloadLog.NEED_LOG = true
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS) // customize the value of the connect timeout.
        builder.readTimeout(15, TimeUnit.SECONDS)
        builder.writeTimeout(15, TimeUnit.SECONDS)
        builder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response {
                val request = chain!!.request().newBuilder()
                        .addHeader("Content-Language", language)
                        .build()
                return chain!!.proceed(request)
            }
        })

        builder.protocols(mutableListOf(Protocol.HTTP_1_1))
        builder
//                .addNetworkInterceptor(StethoInterceptor())
                .build()
        // Init the FileDownloader with the OkHttp3Connection.Creator.
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCountAdapter(FileDownloadHelper.ConnectionCountAdapter(function =
                { _, _, _, _ ->
                    return@ConnectionCountAdapter 1
                }))
                .connectionCreator(OkHttp3Connection.Creator(builder))
    }

    fun configureExceptionLogging() {
        val default = Thread.getDefaultUncaughtExceptionHandler()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            Timber.e(e)
            default.uncaughtException(thread, e)
            System.exit(0)
        }

        RxJavaPlugins.setErrorHandler(Timber::e)
    }

    fun buildComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule())
                .build()
        appComponent.inject(this)
    }

    private fun initCrashHandler() {

        if (!BuildConfig.DEBUG) {
            val handler = CrashHandler.getInstance()
            handler.init(applicationContext, true, api)
            Thread.setDefaultUncaughtExceptionHandler(handler)
        }
    }

    fun isApkInDebug(context: Context): Boolean {
        try {
            val info = context.applicationInfo
            return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            return false
        }
    }

}