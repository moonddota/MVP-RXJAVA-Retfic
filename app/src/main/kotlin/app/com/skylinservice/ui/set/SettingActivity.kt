package app.com.skylinservice.ui.set

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import app.com.skylinservice.BuildConfig
import app.com.skylinservice.BuildConfig.API_URL
import app.com.skylinservice.BuildConfig.SUF_URL_V2
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.exception.appComponent
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.FirstLachManager
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.sqlite.DB
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.set.about.AboutActivity
import app.com.skylinservice.ui.set.changenick.ChangeNickNameActivity
import app.com.skylinservice.ui.set.setfg.EditNickNameFragment
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import app.com.skylinservice.ui.userrelative.login.changephone.ChangePhoneActivity
import app.com.skylinservice.ui.userrelative.login.changpwdbyvalid.ForgotPwdActivity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils

import com.blankj.utilcode.util.ToastUtils
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.item_setting_list.view.*
import kotlinx.android.synthetic.main.item_softcenter_list.view.*
import sjj.permission.PermissionCallback
import sjj.permission.model.Permission
import sjj.permission.util.PermissionUtil
import java.io.File
import java.nio.channels.AlreadyBoundException
import java.text.NumberFormat
import javax.inject.Inject

class SettingActivity : BaseActivity(), SetContract.View {


    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
        }
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

    override fun showRequestAnswer(posts: VlideLogin?) {
        super.showRequestAnswer(posts)
        dialog?.dismiss()

        startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
    }

    var progressBar: ProgressDialog? = null
    override fun showLatesInfo(posts: GetAPPInfoModel) {
        super.showLatesInfo(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200 && posts.data.isNotEmpty()) {
            if (posts.data[0].versionCode.toInt() <= AppUtils.getAppVersionCode()) {
                ToastUtils.showShort(AppUtils.getAppVersionName() + getString(R.string.MainActivity_tv4))
            } else {

                var builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.MainActivity_tv2))
                        .setMessage(getString(R.string.SettingActivity_tv4))
                builder.setPositiveButton(getString(R.string.MainActivity_tv5)) { _, _ ->
                    var count = 2
                    PermissionUtil.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
                        override fun onGranted(permissions: Permission) {
                            if (--count == 0) {
                                beginDownload()
                            }

                        }

                        override fun onDenied(permissions: Permission) {
                            if (permissions.shouldShowRequestPermissionRationale) {
                                ToastUtils.showLong(getString(R.string.MainActivity_tv6) + permissions.name)
                            } else {
                                ToastUtils.showLong(getString(R.string.MainActivity_tv6) + permissions.name)
                            }

                        }
                    })

                }
                builder.setNegativeButton(getString(R.string.MainActivity_tv7)) { _, _ ->
                }
                builder.setCancelable(true)
                builder.show()


//                val REQUEST_EXTERNAL_STORAGE = 1
//                val PERMISSIONS_STORAGE = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                val permission = ActivityCompat.checkSelfPermission(this@SettingActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            this@SettingActivity,
//                            PERMISSIONS_STORAGE,
//                            REQUEST_EXTERNAL_STORAGE
//                    )
//                }

            }

        } else {
            ToastUtils.showShort(getString(R.string.MainActivity_tv8))
        }
    }

    override fun showlogout() {
        super.showlogout()


//        val d = DB(this, "teamstate.db")
//
//        val db = d.writableDatabase
//        db.delete("labelnet", null, null)
//        db.close()


        dialog?.dismiss()
        skylindb.deleteUser(UserIdManager().getUserId().toInt())
        skylindb.deleteTeam()
        UserIdManager().pustUserID(0)
        SPUtils.getInstance().clear()
        FirstLachManager().setFirstLanchState(true)
        SPUtils.getInstance().put("version", AppUtils.getAppVersionCode())
        finishAffinity()

        startActivity(Intent(this@SettingActivity, LoginActivity::class.java))

    }

    private fun beginDownload() {


        progressBar = ProgressDialog.show(this, getString(R.string.MainActivity_tv26)
                , getString(R.string.MainActivity_tv27), false, false)


        val a = "$API_URL$SUF_URL_V2" + "Public/store/?service=App.downApp&id=10"
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
                            progressBar?.setMessage(forresult)

                    }

                    override fun blockComplete(task: BaseDownloadTask?) {}

                    override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {}

                    override fun completed(task: BaseDownloadTask) {
                        ToastUtils.showShort(getString(R.string.MainActivity_tv28))
                        progressBar?.dismiss()
                        AppUtils.installApp(File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "skylinservice2340304p234.apk"), BuildConfig.APPLICATION_ID + ".provider")

                    }

                    override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    }

                    override fun error(task: BaseDownloadTask, e: Throwable) {
                        ToastUtils.showShort(e.message)
                        progressBar?.dismiss()
                    }

                    override fun warn(task: BaseDownloadTask) {}
                }).start()

    }

    interface IOnClick {

        fun click(position: Int)

    }

    @Inject
    lateinit var presenter: SetContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager

    var tmpList: MutableList<MutableList<Any>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        activityComponent.inject(this)
        presenter.attachView(this)
        dialog = ProgressDialog(this)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.setting_activity_title)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)
//        settingRecycle.layoutManager = (LinearLayoutManager(this))

        loggout.text = getString(R.string.main_activity_devicemanager_dialog_btn_name)

        //mutableListOf("修改密码", "", false),

        tmpList = mutableListOf(mutableListOf(getString(R.string.setting_activity_dialog_key), skylindb.getUserById(UserIdManager().getUserId()).name, true)
                , mutableListOf(getString(R.string.activity_changephone_title), "", false)
                , mutableListOf(getString(R.string.SettingActivity_tv5), AppUtils.getAppVersionName(), true)
                , mutableListOf(getString(R.string.setting_activity_about), "", false))

//        settingRecycle.adapter = MainAdapter(object : SettingActivity.IOnClick {
//            override fun click(position: Int) {
//                when (position) {
//                    0 -> {
//                        var intent = Intent(this@SettingActivity, ChangeNickNameActivity::class.java)
//                        startActivityForResult(intent, 108)
//
//                    }
////                    1 -> startActivity(Intent(this@SettingActivity, ForgotPwdActivity::class.java))
//                    1 -> startActivity(Intent(this@SettingActivity, ChangePhoneActivity::class.java))
//                    2 -> presenter.checkUpdate(1)
//                    3 -> startActivity(Intent(this@SettingActivity, AboutActivity::class.java))
//                }
//
//            }
//
//        }, tmpList!!)

        checck_cotent.text = AppUtils.getAppVersionName()

        nick_rl.setOnClickListener {
            var intent = Intent(this@SettingActivity, ChangeNickNameActivity::class.java)
            startActivityForResult(intent, 108)
        }

        changephone_rl.setOnClickListener {
            startActivity(Intent(this@SettingActivity, ChangePhoneActivity::class.java))
        }

        abount_rl.setOnClickListener {
            startActivity(Intent(this@SettingActivity, AboutActivity::class.java))
        }

        cehcek_rl.setOnClickListener {
            presenter.checkUpdate(10)
        }

        loggout.setOnClickListener {
            createDialog(getString(R.string.main_activity_devicemanager_dialog_btn_name)
                    , getString(R.string.SettingActivity_tv6))

        }

        skylin_content.text = skylindb.getUserById(UserIdManager().getUserId()).id.toString()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 108)
            setResult(108, data)
    }

//   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == resultCode && requestCode == 108) {
//            tmpList!![0][1] = skylindb.getUserById(UserIdManager().getUserId()).name
//            settingRecycle.adapter.notifyDataSetChanged()
//
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }

    override fun onResume() {
        super.onResume()
        if (UserIdManager().getUserId().toInt() != 0) {
//            tmpList!![0][1] = skylindb.getUserById(UserIdManager().getUserId()).name
            nick_content.visibility = View.VISIBLE
            nick_arrarow.visibility = View.INVISIBLE
            nick_content.text = skylindb.getUserById(UserIdManager().getUserId()).name
        }
        // settingRecycle.adapter.notifyDataSetChanged()

    }


    private fun createDialog(title: String, content: String) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(content)
        builder.setPositiveButton(getString(R.string.activity_teammanager_btn_createteam_btn_name)) { _, _ ->
            presenter.logout()
        }
        builder.setNegativeButton(getString(R.string.cancle)) { _, _ ->
        }
        builder.show()
    }

    private fun startWebView() {
//        val intent = Intent(this, WebActivity::class.java)
//        intent.putExtra(WebActivity.urlKey, getString(R.string.skylinuav_url))
//        startActivity(intent)
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val content_url = Uri.parse("http://www.skylinuav.com")
        intent.data = content_url
        startActivity(intent)

    }

    inner class MainAdapter(val itemClick: IOnClick, private val items: MutableList<MutableList<Any>>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindForecast(items[position], position)


        }

        override fun getItemCount(): Int {
            return items.size
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bindForecast(item: MutableList<Any>, position: Int) {
                with(item) {
                    if (item[2] as Boolean) {
                        itemView.setting_iv_arrarow.visibility = View.INVISIBLE
                        itemView.setting_tv_content.visibility = View.VISIBLE
                        itemView.setting_tv_title.text = item[0].toString()
                        itemView.setting_tv_content.text = item[1].toString()
                    } else {
                        itemView.setting_iv_arrarow.visibility = View.VISIBLE
                        itemView.setting_tv_content.visibility = View.INVISIBLE
                        itemView.setting_tv_title.text = item[0].toString()
                    }
                    itemView.setOnClickListener { itemClick.click(position) }

                }
            }
        }


    }
}