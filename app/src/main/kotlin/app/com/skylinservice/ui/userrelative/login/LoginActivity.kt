package app.com.skylinservice.ui.userrelative.login

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import app.com.skylinservice.AppApplication
import app.com.skylinservice.BuildConfig
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.TrueNameAuthedManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.device.scan.ScanCodeActivity
import app.com.skylinservice.ui.main.MainActivity
import app.com.skylinservice.ui.userrelative.login.changpwdbyvalid.ForgotPwdActivity
import app.com.skylinservice.ui.userrelative.login.regist.RegistAccountActivity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern
import javax.inject.Inject
import com.tencent.mm.opensdk.modelmsg.SendAuth
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import sjj.alog.Log
import sjj.permission.PermissionCallback
import sjj.permission.model.Permission
import sjj.permission.util.PermissionUtil
import java.io.File
import java.text.NumberFormat
import java.util.concurrent.TimeUnit


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), LoginContract.View {
    override fun showweixinConnect(post: WeixinConnectModel) {
    }
    override fun showweixinBind(post: WeixinBindModel) {
    }

    var aa: Disposable? = null
    override fun showSend(posts: VlideNumModel) {

        ToastUtils.showLong(GsonUtils.createGsonString(posts))

        if (posts.ret.toInt() == 200) {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv24))
            launch(CommonPool) {
                Thread.sleep(1000)
                var timeTotal = 60
                aa = Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (timeTotal > 0) {
                                regit_tv_getvalid.isEnabled = false
                                regit_tv_getvalid.text = timeTotal--.toString()
                            } else {
                                regit_tv_getvalid.text = getString(R.string.action_sign_getvalidnum)
                                aa?.dispose()
                                regit_tv_getvalid.isEnabled = cellphoneMatches(cellphone.text.toString())

                            }
                        }, { e ->
                            aa?.dispose()


                        })
            }
        } else {
            ToastUtils.showShort(posts.msg)
        }

    }

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
    }

    var dialog: ProgressDialog? = null

    override fun setLoadingIndicatorVisible(loadingVisible: Boolean) {
        if (loadingVisible) {
            dialog?.setCancelable(false)
            dialog?.show()
        } else {
            dialog?.dismiss()
        }


    }

    override fun showNoConnectivityError() {
        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv3))
        dialog?.dismiss()
    }

    override fun showUnknownError() {
        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv4))
        dialog?.dismiss()
    }


    override fun showRequestAnswer(posts: VlideLogin) {
        super.showRequestAnswer(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            UserIdManager().pustUserID(posts.data.id.toLong())
            skylindb.update(posts.data)
            SPUtils.getInstance().put("token", posts.data.token)
            SPUtils.getInstance().put("userName", posts.data.name)
            TrueNameAuthedManager().setFirstLanchState(posts.data.relStatus.toInt())
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finishAffinity()
            aa?.dispose()
        } else {
            ToastUtils.showShort("")
        }
    }

    var builder: AlertDialog.Builder? = null


    override fun showLatesInfo(posts: GetAPPInfoModel) {
        super.showLatesInfo(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200 && posts.data.isNotEmpty()) {
            if (posts.data[0].versionCode.toInt() <= AppUtils.getAppVersionCode()) {
                ToastUtils.showShort(AppUtils.getAppVersionName() + "当前已经是最新版本")
            } else {

                builder = AlertDialog.Builder(this)
                builder!!.setTitle(getString(R.string.MainActivity_tv2))
                        .setMessage(getString(R.string.MainActivity_tv3))
                builder!!.setPositiveButton(getString(R.string.MainActivity_tv5)) { _, _ ->
                    var count = 2
                    PermissionUtil.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
                        override fun onGranted(permissions: Permission) {
                            if (--count == 0) {
                                beginDownload()
                            }

                        }

                        override fun onDenied(permissions: Permission) {
                            if (permissions.shouldShowRequestPermissionRationale) {
                                ToastUtils.showLong(getString(R.string.MainActivity_tv6)+":" + permissions.name)
                            } else {
                                ToastUtils.showLong(getString(R.string.MainActivity_tv6)+":" + permissions.name)

                            }

                        }
                    })

                }
                builder!!.setNegativeButton(getString(R.string.MainActivity_tv7)) { _, _ ->
                }
                builder!!.setCancelable(true)
                builder!!.show()


            }

        } else {
            ToastUtils.showShort(getString(R.string.MainActivity_tv8))
        }
    }

    private fun beginDownload() {


        val dialog4 = ProgressDialog.show(this, getString(R.string.MainActivity_tv26)
                , getString(R.string.MainActivity_tv27), false, false)


        val a = "${BuildConfig.API_URL}${BuildConfig.SUF_URL_V2}" + "Public/store/?service=App.downApp&id=10"
        FileDownloader.getImpl().create(a)
                .setForceReDownload(true)
                .setPath(Environment.getExternalStorageDirectory().absolutePath + File.separator + "skylinservice2340304p234.apk")
                .setListener(object : FileDownloadListener() {
                    override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    }

                    override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {

                    }

                    override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {

                        val numberFormat = NumberFormat.getInstance()
                        // 设置精确到小数点后2位
                        numberFormat.maximumFractionDigits = 2
                        val result = numberFormat.format(soFarBytes.toFloat() / totalBytes.toFloat())
                        var forresult = (result.toFloat() * 100).toString() + "%"
                        if (forresult.length <= 5)
                            dialog4.setMessage(forresult)

                    }

                    override fun blockComplete(task: BaseDownloadTask?) {}

                    override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {}

                    override fun completed(task: BaseDownloadTask) {
                        builder?.create()?.dismiss()
                        ToastUtils.showShort(getString(R.string.MainActivity_tv28))
                        dialog4.dismiss()
                        AppUtils.installApp(File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "skylinservice2340304p234.apk"), BuildConfig.APPLICATION_ID + ".provider")

                    }

                    override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    }

                    override fun error(task: BaseDownloadTask, e: Throwable) {
                        ToastUtils.showShort(e.message)
                        builder?.create()?.dismiss()
                        dialog4.dismiss()
                    }

                    override fun warn(task: BaseDownloadTask) {}
                }).start()

    }

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    @Inject
    lateinit var presenter: LoginContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager
    var phoenumberReady = false
    var pwdReady = false
    var validnumReady = false

    var isRequesOK = false

    var myrefvier: MyReceiver? = null

    var loginActivity :  LoginActivity ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginActivity = this

        activityComponent.inject(this)
        presenter.attachView(this)

        dialog = ProgressDialog(this)


        var count = 3
        PermissionUtil.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
            override fun onGranted(permissions: Permission) {
                if (--count == 0) {
                    isRequesOK = true
                }

            }

            override fun onDenied(permissions: Permission) {
                if (permissions.shouldShowRequestPermissionRationale) {
                    ToastUtils.showLong(getString(R.string.MainActivity_tv6)+":" + permissions.name)
                    isRequesOK = false
                } else {
                    ToastUtils.showLong(getString(R.string.MainActivity_tv6)+":" + permissions.name)
                    isRequesOK = false

                }

            }
        })


//        RxPermissions.getInstance(this)
//                .request(Manifest.permission.CAMERA,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe({ granted ->
//                    if (granted) {
//                        login_iv_weixin_login.isEnabled = true
//
//                    } else {
//                        ToastUtils.showShort("请打开权限")
//                        login_iv_weixin_login.isEnabled = false
//                        finish()
//                    }
//                })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.white)

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }

        login_iv_weixin_login.setOnClickListener { email_sign_in_button.isEnabled = true }

//        login_tv_registnew.setOnClickListener { startActivity(Intent(this@LoginActivity, RegistAccountActivity::class.java)) }

//        login_tv_forggotpwd.setOnClickListener { startActivity(Intent(this@LoginActivity, ForgotPwdActivity::class.java)) }

        email_sign_in_button.setOnClickListener {

            //            if (!isRequesOK) {
//                ToastUtils.showShort("请到设置界面打开应用权限")
//                return@setOnClickListener
//            }

            if (phoenumberReady && validnumReady) {
                presenter.login(cellphone.text.toString(), reigist_edit.text.toString())

            } else
                ToastUtils.showLong(getString(R.string.TeamManagerActivity_tv30))
        }


        cellphone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                phoenumberReady = p0.toString().isNotEmpty()
                handleCommitBtnState()
                handleGetValidNumTVState(p0.toString())
                if (phoenumberReady && !cellphoneMatches(p0.toString()))
                    cellphone.setError(getString(R.string.title_activity_error_phoneformat), null)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        reigist_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                validnumReady = p0.toString().length == 4
                handleCommitBtnState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


//        password.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                var isEmoji = isEmoji(p0.toString())
//                pwdReady = p0.toString().length >= 6 && !isEmoji
//                if (isEmoji) {
//                    password.setError("不能包含表情符")
//                    ToastUtils.showShort("密码中不能包含表情符")
//                }
//                handleCommitBtnState()
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//        })
        regit_tv_getvalid.setOnClickListener {
            presenter.sendValidNum(cellphone.text.toString())
        }


        login_iv_weixin_login.setOnClickListener {
            //            if (!isRequesOK) {
//                ToastUtils.showShort("请到设置界面打开应用权限")
//                return@setOnClickListener
//            }

            if (!AppApplication.mWxApi?.isWXAppInstalled()!!) {
                ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv31))
                return@setOnClickListener
            }
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "skylinservice_wx_login"
            AppApplication.mWxApi?.sendReq(req)

        }

        myrefvier = MyReceiver()
        var filter = IntentFilter()
        filter.addAction("closeLogin")
        registerReceiver(myrefvier, filter)

        presenter.checkUpdate(10)


        // Set up the login form.
    }

    fun isEmoji(string: String): Boolean {
        val p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        val m = p.matcher(string)
        return m.find()
    }


    override fun onDestroy() {
        super.onDestroy()
        loginActivity = null
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
        unregisterReceiver(myrefvier)
    }

    private fun handleGetValidNumTVState(phone: String) {
        regit_tv_getvalid.isEnabled = phoenumberReady && cellphoneMatches(phone) && regit_tv_getvalid.text == getString(R.string.action_sign_getvalidnum)
    }

    private fun cellphoneMatches(phone: String): Boolean {

//        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(1[0-9][0-9]))\\d{8}$"
//        val p = Pattern.compile(regExp)
//        val m = p.matcher(phone)
//        return m.matches()
        return phone.isNotEmpty()
    }


    private fun handleCommitBtnState() {
        email_sign_in_button.isEnabled = phoenumberReady && validnumReady
    }


   inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals("closeLogin")) {
                loginActivity?.finish()
                loginActivity = null
            }
        }

    }


}