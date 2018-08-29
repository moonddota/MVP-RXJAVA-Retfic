package app.com.skylinservice.injection.module

import android.content.Context
import android.net.ConnectivityManager
import app.com.skylinservice.AppApplication
import app.com.skylinservice.BuildConfig
import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.isConnected
import app.com.skylinservice.manager.CustomerGsonConvertFactory.CustomGsonConverterFactory
import app.com.skylinservice.manager.SkyLinDBManager
import com.google.gson.Gson
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.xutils.DbManager
import org.xutils.x
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


@Module
class ApiModule {

    fun getOkHttpBuilder(): OkHttpClient.Builder {
        var aa: OkHttpClient.Builder? = null

        val xtm = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                val x509Certificates = arrayOfNulls<X509Certificate>(0)
                return x509Certificates
            }
        }

        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")

            sslContext!!.init(null, arrayOf<TrustManager>(xtm), SecureRandom())

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        val DO_NOT_VERIFY = object : HostnameVerifier {
            override fun verify(hostname: String, session: SSLSession): Boolean {
                return true
            }
        }




        aa = OkHttpClient.Builder()

        aa.addInterceptor(HttpLoggingInterceptor()
                .apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }
        )
                ?.retryOnConnectionFailure(true)
                ?.connectTimeout(15, TimeUnit.SECONDS)
                ?.sslSocketFactory(sslContext?.socketFactory)
                ?.hostnameVerifier(DO_NOT_VERIFY)
                ?.build()
        return aa
    }


    @Provides
    @Singleton
    fun providesOkHttpClient(app: AppApplication): OkHttpClient {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return getOkHttpBuilder()
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()

                    if (!connectivityManager.isConnected) {
                        throw NoNetworkException
                    }

                    try {
                        chain.proceed(requestBuilder.build())
                    } catch (e: SocketTimeoutException) {
                        throw NoNetworkException
                    } catch (e: UnknownHostException) {
                        throw NoNetworkException
                    } catch (e: SSLPeerUnverifiedException) {
                        throw NoNetworkException
                    }
                }
                .build()
    }

    @Provides
    @Singleton
    fun providesAppApi(okHttpClient: OkHttpClient, gson: Gson): Api {

        val builder = OkHttpClient.Builder()
        builder.connectTimeout(5000, TimeUnit.SECONDS)  //连接超时时间
        builder.readTimeout(5000, TimeUnit.SECONDS)  //读取超时时间
        builder.writeTimeout(5000, TimeUnit.SECONDS)  //写入超时时间
        builder.addInterceptor(object  :Interceptor{
            override fun intercept(chain: Interceptor.Chain?): Response {
                var  request  = chain!!.request().newBuilder()
                        .addHeader("Content-Language",AppApplication.app.language)
                        .build();
                return chain!!.proceed(request);
            }
        })


        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(CustomGsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.API_URL)
//                .client(okHttpClient)
                .client(builder.build())
                .build()

        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun providesImageLoader(context: Context): ImageLoader {
        val configuration = ImageLoaderConfiguration.createDefault(context)
        var aaa = ImageLoader.getInstance()
        aaa.init(configuration)
        return aaa
    }


    fun getDBManager(): DbManager {
        val daoConfig = DbManager.DaoConfig()
                .setDbName("skylinservice.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                //                .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                .setDbVersion(1)
                .setDbOpenListener { db ->
                    // 开启WAL, 对写入加速提升巨大
                    db.database.enableWriteAheadLogging()
                }
                .setDbUpgradeListener { db, oldVersion, newVersion ->
                    //第一次升级数据库，升级飞行记录字段
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }

        return x.getDb(daoConfig)
    }

    @Provides
    @Singleton
    fun providesSkyLinDBManager(): SkyLinDBManager {

        return SkyLinDBManager(getDBManager())
    }


}