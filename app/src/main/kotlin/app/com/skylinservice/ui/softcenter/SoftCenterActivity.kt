package app.com.skylinservice.ui.softcenter

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import app.com.skylinservice.AppApplication
import app.com.skylinservice.BuildConfig
import app.com.skylinservice.BuildConfig.API_URL
import app.com.skylinservice.BuildConfig.SUF_URL_V2
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.model.APPModel
import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.GetAPPsModel
import app.com.skylinservice.data.remote.requestmodel.SoftApps
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer
import kotlinx.android.synthetic.main.activity_soft_center.*
import kotlinx.android.synthetic.main.item_softcenter_list.view.*
import java.io.File
import java.net.SocketTimeoutException
import java.security.interfaces.RSAKey
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


class SoftCenterActivity : BaseActivity(), SoftCenterContract.View {


    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@SoftCenterActivity, LoginActivity::class.java))
        }
    }

    var dialog: ProgressDialog? = null

    override fun setLoadingIndicatorVisible(loadingVisible: Boolean) {
        runOnUiThread {
            if (loadingVisible) {
                dialog?.setCancelable(false)
                dialog?.show()
            } else {
                dialog?.dismiss()
            }
        }


    }

    override fun showNoConnectivityError() {
        runOnUiThread {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv3))
            dialog?.dismiss()
        }
    }

    override fun showUnknownError() {
        runOnUiThread {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv4))
            dialog?.dismiss()
        }
    }


    override fun showgetSoftApp(posts: GetAPPsModel) {
        super.showgetSoftApp(posts)

//        AppApplication.app.tmpList.clear()
        for (soft in posts.data[0]) {
            if (soft.type.equals("app")
                    && !soft.name.equals("软件中心")
                    && !soft.name.equals("天麒服务")
                    && !soft.name.equals("测量大师")
            ) {
                var hasOne = false
                for (index in AppApplication.app.tmpList.indices) {
                    if (AppApplication.app.tmpList[index].id == soft.id) {
                        if (AppApplication.app.tmpList[index].task != null && AppApplication.app.tmpList[index].version != soft.version) {
                            AppApplication.app.tmpList[index].task?.listener = null
                            FileDownloader.getImpl().clear(AppApplication.app.tmpList[index].task?.id!!, AppApplication.app.tmpList[index].task?.targetFilePath)
                        }
                        hasOne = true
                        break
                    }
                }
                if (!hasOne)
                    AppApplication.app.tmpList.add(soft)
            }
        }
        runOnUiThread {
            dialog?.dismiss()
            app_recyclerview.adapter = MainAdapter(object : IOnClick {
                override fun click(position: Int) {


                }

            }, object : IOnLongClick {
                override fun click(position: Int) {
                    var builder = AlertDialog.Builder(this@SoftCenterActivity)
                    builder.setTitle(getString(R.string.SettingActivity_tv8))
                            .setMessage(getString(R.string.SettingActivity_tv9))
                    builder.setPositiveButton(getString(R.string.activity_teammanager_btn_createteam_btn_name)) { _, _ ->
                        var item = AppApplication.app.tmpList.get(position)
                        var b = Environment.getExternalStorageDirectory().absolutePath + File.separator + item.name + ".apk"
                        FileUtils.deleteFile(b)
                        app_recyclerview?.adapter?.notifyDataSetChanged()

                    }
                    builder.setNegativeButton(getString(R.string.cancle)) { _, _ ->

                    }
                    builder.show()

                }

            }, AppApplication.app.tmpList)
            makeStatePerfect()
            toolbar.invalidate()

            app_recyclerview.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if ((newState == RecyclerView.SCROLL_STATE_IDLE))
                        for (index in AppApplication.app.tmpList.indices) {
                            if (AppApplication.app.tmpList[index].task != null && AppApplication.app.tmpList[index].install_state == 4) {
                                softNotifydatachanged(AppApplication.app.tmpList[index].task!!)
                            }
                        }
                }
            })
        }
    }


    override fun onResume() {
        super.onResume()
        app_recyclerview?.adapter?.notifyDataSetChanged()

    }

    var isFinish = false

    override fun onBackPressed() {
        if (AppApplication.app.tmpList?.size > 0) {
            for (soft in AppApplication.app.tmpList) {
                if (soft.install_state == 4 ||
                        soft.install_state == 5 ||
                        soft.install_state == 6) {
                    soft.task?.listener = null
                }
            }
        }
        isFinish = true
        super.onBackPressed()


    }

    override fun showgetSoftAppDetail(posts: GetAPPInfoModel) {
        super.showgetSoftAppDetail(posts)
        if (posts.ret.toInt() == 200) {
            if (posts.data[0].versionCode.toInt() <= AppUtils.getAppVersionCode()) {
                ToastUtils.showShort(AppUtils.getAppVersionName() + getString(R.string.MainActivity_tv4))
            } else {
            }

        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    @Inject
    lateinit var presenter: SoftCenterContract.Presenter

    lateinit var mytmpList: MutableList<SoftApps>


    @Inject
    lateinit var imageloader: ImageLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soft_center)

        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = getString(R.string.activity_softcenter_title)

        activityComponent.inject(this)
        presenter.attachView(this)

        dialog = ProgressDialog(this)

        app_pullrefresh.setOnRefreshListener {
            if (app_recyclerview != null && app_recyclerview.adapter != null)
                app_recyclerview.adapter.notifyDataSetChanged()
            app_pullrefresh.setRefreshing(false)
        }

        app_recyclerview.layoutManager = (LinearLayoutManager(this))

        val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission = ActivityCompat.checkSelfPermission(this@SoftCenterActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this@SoftCenterActivity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            )
        }

        FileDownloader.getImpl().bindService()

//        if (AppApplication.app.tmpList.size != 0) {
//
//            dialog?.dismiss()
//            app_recyclerview.adapter = MainAdapter(object : IOnClick {
//                override fun click(position: Int) {
//
//
//                }
//
//            }, object : IOnLongClick {
//                override fun click(position: Int) {
//                    var builder = AlertDialog.Builder(this@SoftCenterActivity)
//                    builder.setTitle("删除?")
//                            .setMessage("是否删除此APP缓存")
//                    builder.setPositiveButton("确认") { _, _ ->
//                        var item = AppApplication.app.tmpList.get(position)
//                        var b = Environment.getExternalStorageDirectory().absolutePath + File.separator + item.name + ".apk"
//                        FileUtils.deleteFile(b)
//                        app_recyclerview?.adapter?.notifyDataSetChanged()
//
//                    }
//                    builder.setNegativeButton("取消") { _, _ ->
//
//                    }
//                    builder.show()
//
//                }
//
//            }, AppApplication.app.tmpList)
//            makeStatePerfect()
//            toolbar.invalidate()
//
//
//
//
//            app_recyclerview.setOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    if ((newState == RecyclerView.SCROLL_STATE_IDLE))
//                        for (index in AppApplication.app.tmpList.indices) {
//                            if (AppApplication.app.tmpList[index].task != null && AppApplication.app.tmpList[index].install_state == 4) {
//                                softNotifydatachanged(AppApplication.app.tmpList[index].task!!)
//                            }
//                        }
//
//                }
//
//
//            })
//        } else {
        presenter.etFinalApps()

//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.


        menuInflater.inflate(R.menu.softcenter, menu)
        if (needAllUpdate())
            menu?.getItem(0)?.title = getString(R.string.SettingActivity_tv10)
        else
            menu?.getItem(0)?.title = getString(R.string.activity_softcenter_menu_title)

        menu?.getItem(0)?.isEnabled = needAllDownload()
        return true
    }

    var menu: Menu? = null

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        super.onPrepareOptionsMenu(menu)
        if (needAllUpdate())
            menu?.getItem(0)?.title = getString(R.string.SettingActivity_tv10)
        else
            menu?.getItem(0)?.title = getString(R.string.activity_softcenter_menu_title)

        menu?.getItem(0)?.isEnabled = needAllDownload()

        return true

    }


    fun needAllDownload(): Boolean {
        var hasNeedDownload = false
        for (item in AppApplication.app.tmpList) {
            if (item.install_state == 2 || item.install_state == 0 || item.install_state == 5)
            //|| item.install_state == 4
                hasNeedDownload = true
        }

        return hasNeedDownload
    }

    fun needAllUpdate(): Boolean {
        var update = false
        for (item in AppApplication.app.tmpList) {
            if (item.install_state == 2
            )
                update = true
        }

        return update
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {


                makeStatePerfect(item.title!! == getString(R.string.SettingActivity_tv10))

                loop@ for (index in AppApplication.app.tmpList.indices) {

                    when (AppApplication.app.tmpList[index].install_state) {
                        1, 3 -> {
                            continue@loop
                        }
                        4 -> {
                            begindownload(AppApplication.app.tmpList[index])
                        }
                        5 -> {
                            if (AppApplication.app.tmpList[index].task != null) {
                                try {
                                    AppApplication.app.tmpList[index].task!!.reuse()
                                    AppApplication.app.tmpList[index].task!!.start()
                                    AppApplication.app.tmpList[index].install_state = 4
                                } catch (ill: IllegalStateException) {
                                    ToastUtils.showLong(getString(R.string.SettingActivity_tv11)+":" + AppApplication.app.tmpList[index].task?.filename)
                                    continue@loop
                                }
                                continue@loop
                            }
                        }
                    }
//                    if (AppApplication.app.tmpList[index].install_progress > 0)
//                        break
                }
                app_recyclerview.adapter.notifyDataSetChanged()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeStatePerfect() {
        loop@ for (index in AppApplication.app.tmpList.indices) {
            if (isInstalled(AppApplication.app.tmpList[index])) {
                if (isNeedUpdate(AppApplication.app.tmpList[index])) {
                    if (isDownloadOK(AppApplication.app.tmpList[index])) {
                        AppApplication.app.tmpList[index].install_state = 3
                    } else if (AppApplication.app.tmpList[index].install_state != 4 && AppApplication.app.tmpList[index].install_state != 5 && AppApplication.app.tmpList[index].install_state != 6) {
                        AppApplication.app.tmpList[index].install_state = 2

                    }
                } else {
                    AppApplication.app.tmpList[index].install_state = 1
                }
            } else {
                if (isDownloadOK(AppApplication.app.tmpList[index])) {
                    AppApplication.app.tmpList[index].install_state = 3
                } else if (AppApplication.app.tmpList[index].install_state != 4 && AppApplication.app.tmpList[index].install_state != 5 && AppApplication.app.tmpList[index].install_state != 6) {
                    AppApplication.app.tmpList[index].install_state = 0
                }
            }
        }
    }


    private fun makeStatePerfect(justUpdate: Boolean) {
        loop@ for (index in AppApplication.app.tmpList.indices) {
            if (justUpdate) {
                if (AppApplication.app.tmpList[index].install_state == 2) {
                    AppApplication.app.tmpList[index].install_state = 4
                }
            } else {
                if (AppApplication.app.tmpList[index].install_state == 0) {
                    AppApplication.app.tmpList[index].install_state = 4
                }
            }

        }


    }


    private fun begindownload(item: SoftApps) {
        val a = "$API_URL$SUF_URL_V2" + "Public/store/?service=App.downApp&id=" + item.id
        FileDownloader.getImpl().setMaxNetworkThreadCount(1)
        FileDownloader.getImpl().create(a)
                .setForceReDownload(true)
                .setAutoRetryTimes(2)
                .setPath(Environment.getExternalStorageDirectory().absolutePath + File.separator + item.name + ".apk")
                .setListener(object : FileDownloadListener() {
                    override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                        item.task = task
                        item.task?.tag = item.id
                        softNotifydatachanged(task)
                    }

                    override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                        item.install_state = 6

                    }

                    override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
//                                            itemView.softcenter_download_loadFrame.visibility = View.VISIBLE
//                                            itemView.softcenter_download_state.visibility = View.INVISIBLE
                        if (item.install_state == 5)
                            return
                        val numberFormat = NumberFormat.getInstance()
                        // 设置精确到小数点后2位
                        numberFormat.maximumFractionDigits = 2
                        val result = numberFormat.format(soFarBytes.toFloat() / totalBytes.toFloat())
                        try {
                            item.install_progress = ((result.toFloat() * 100).toInt())
                        } catch (e: Exception) {
                            item.install_progress = 0
                        } finally {
                            softNotifydatachanged(task)

                        }

                    }

                    override fun blockComplete(task: BaseDownloadTask?) {}

                    override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {}

                    override fun completed(task: BaseDownloadTask) {
                        item.install_state = 3
                        item.install_progress = 0
                        item.task = null
                        softNotifydatachanged(task)
                        AppUtils.installApp(task.targetFilePath, BuildConfig.APPLICATION_ID + ".provider");


                    }

                    override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
//                        item.install_state = 5
//                        softNotifydatachanged(task)
                    }

                    override fun error(task: BaseDownloadTask, e: Throwable) {
                        if (e.message != null && e is FileDownloadGiveUpRetryException && e.message!!.startsWith("sofar")) {
                            task.reuse()
                            task.start()
                        } else {
                            FileDownloader.getImpl().clear(task.id, task.targetFilePath)
                            item.install_progress = 0
                            item.install_state = 5
                            softNotifydatachanged(task)
                        }


                    }

                    override fun warn(task: BaseDownloadTask) {}
                }).start()

    }

    inner class MainAdapter(val itemClick: IOnClick, val itemLongClick: IOnLongClick, private var items: List<SoftApps>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_softcenter_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (holder.itemView.tag == items[position].id) {
                holder.bindForecast(items[position], position)

            } else {
                holder.bindForecast(items[position], position)
            }


        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return items.size
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bindForecast(item: SoftApps, position: Int) {
                with(item) {
                    itemView.tag = item.id
                    itemView.setOnLongClickListener {
                        itemLongClick.click(position)
                        true
                    }
                    itemView.softcent_app_icon.setImageResource(R.mipmap.naza)
                    itemView.softcent_app_name.text = item.name
                    itemView.softcent_app_des.text = item.info
                    val options = DisplayImageOptions.Builder()
                            .showStubImage(R.mipmap.icon_default)
                            .showImageForEmptyUri(R.mipmap.icon_default)     //url爲空會显示该图片，自己放在drawable里面的
                            .showImageOnFail(R.mipmap.icon_default)                //加载图片出现问题，会显示该图片
                            .cacheInMemory()                                               //缓存用
                            .cacheOnDisc()                                                    //缓存用
                            .displayer(RoundedBitmapDisplayer(5))       //图片圆角显示，值为整数
                            .build()
                    if (item.icon.startsWith("http")) {
                        imageloader.displayImage(item.icon, itemView.softcent_app_icon, options)
                    } else {
                        imageloader.displayImage(API_URL + item.icon, itemView.softcent_app_icon, options)

                    }



                    makeBaseState(item)


                    when (item.install_state) {
                        1 -> {
                            itemView.softcenter_download_state.text = getString(R.string.SettingActivity_tv12)
                            itemView.softcenter_download_loadFrame.visibility = View.INVISIBLE
                            itemView.softcenter_download_state.visibility = View.VISIBLE
                            itemView.waitingiv.visibility = View.INVISIBLE
                            itemView.waitingiv.clearAnimation()


                        }
                        2 -> {
                            itemView.softcenter_download_state.text = getString(R.string.MainActivity_tv2)
                            itemView.softcenter_download_loadFrame.visibility = View.INVISIBLE
                            itemView.softcenter_download_state.visibility = View.VISIBLE
                            itemView.waitingiv.visibility = View.INVISIBLE
                            itemView.waitingiv.clearAnimation()


                        }
                        3 -> {
                            Handler().postDelayed(Runnable {
                                runOnUiThread {
                                    itemView.softcenter_download_loadFrame.visibility = View.INVISIBLE
                                    itemView.softcenter_download_state.visibility = View.VISIBLE
                                    itemView.softcenter_download_state.text = getString(R.string.SettingActivity_tv13)
                                    itemView.waitingiv.visibility = View.INVISIBLE
                                    itemView.waitingiv.clearAnimation()
                                }
                            }, 300)


                        }
                        4 -> {

                            if (item.task != null && item.task!!.tag == itemView.tag) {
                                itemView.waitingiv.clearAnimation()
                                itemView.softcenter_download_loadFrame.visibility = View.VISIBLE
                                itemView.softcenter_download_state.visibility = View.INVISIBLE
                                itemView.stateiv.visibility = View.INVISIBLE
                                itemView.softcenter_download_prgrogress.visibility = View.INVISIBLE
                                itemView.waitingiv.visibility = View.VISIBLE
                                itemView.waitingiv.setImageResource(R.mipmap.icon_waiting)
                                var operatingAnim = AnimationUtils.loadAnimation(this@SoftCenterActivity, R.anim.rotate_anim);
                                var lin = LinearInterpolator()
                                operatingAnim.interpolator = lin
                                itemView.waitingiv.animation = operatingAnim
                                itemView.waitingiv.startAnimation(operatingAnim)


                            }


                        }
                        5 -> {

                            if (item.task != null && item.task!!.tag == itemView.tag) {
                                itemView.softcenter_download_loadFrame.visibility = View.VISIBLE
                                itemView.softcenter_download_loadFrame.setBackgroundDrawable(null)
                                itemView.softcenter_download_state.visibility = View.INVISIBLE
                                itemView.stateiv.setImageResource(R.mipmap.icon_pause)
                                itemView.softcenter_download_prgrogress.progress = item.install_progress
                                itemView.softcenter_download_prgrogress.visibility = View.VISIBLE
                                itemView.stateiv.visibility = View.VISIBLE
                                itemView.waitingiv.visibility = View.INVISIBLE
                                itemView.waitingiv.clearAnimation()


                            }


                        }
                        0 -> {
                            itemView.softcenter_download_state.text = getString(R.string.MainActivity_tv26)
                            itemView.softcenter_download_loadFrame.visibility = View.INVISIBLE
                            itemView.softcenter_download_state.visibility = View.VISIBLE
                            itemView.waitingiv.visibility = View.INVISIBLE
                            itemView.waitingiv.clearAnimation()


                        }
                        6 -> {
                            if (item.task != null && item.task!!.tag == itemView.tag) {

                                itemView.softcenter_download_loadFrame.visibility = View.VISIBLE
                                itemView.softcenter_download_loadFrame.setBackgroundDrawable(null)
                                itemView.softcenter_download_state.visibility = View.INVISIBLE
                                itemView.softcenter_download_prgrogress.progress = item.install_progress
                                itemView.stateiv.setImageResource(R.mipmap.icon_continue)
                                itemView.softcenter_download_prgrogress.visibility = View.VISIBLE
                                itemView.stateiv.visibility = View.VISIBLE
                                itemView.waitingiv.visibility = View.INVISIBLE
                                itemView.waitingiv.clearAnimation()


                            }


                        }

                    }

                    onPrepareOptionsMenu(menu)



                    itemView.softcenter_download_loadFrame.setOnClickListener {
                        if (item.install_state == 4) {
                            if (item.task != null) {
                                if (item.task?.pause()!!) {
                                    item.install_state = 5
                                    softNotifydatachanged(item.task!!)
                                }
                            } else {
                                item.install_state = 6
                            }


                        } else if (item.install_state == 5 && !item.task?.isRunning!!) {
                            item.task?.reuse()
                            item.task?.start()
                            item.install_state = 6
                            softNotifydatachanged(item.task!!)

                        } else {
                            if (item.task != null) {
                                if (item.task?.pause()!!) {
                                    item.install_state = 5
                                    softNotifydatachanged(item.task!!)
                                }
                            }

                        }

                    }
                    itemView.softcenter_download_state.setOnClickListener {

                        if (item.install_state == 1) {

                            AppUtils.launchApp(this@SoftCenterActivity, item.detials[0].packageName, 0)
                            return@setOnClickListener
                        }


                        if (item.install_state == 4) {
                            if (item.task != null) {
                                if (item.task?.pause()!!) {
                                    item.install_state = 5
                                    softNotifydatachanged(item.task!!)
                                }
                            } else {
                                item.install_state = 6
                            }

                        } else if (item.install_state == 5 && !item.task?.isRunning!!) {
                            item.task?.reuse()
                            item.task?.start()
                            item.install_state = 6
                            softNotifydatachanged(item.task!!)


                        } else if (item.install_state == 3) {

                            AppUtils.installApp(File(Environment.getExternalStorageDirectory().absolutePath + File.separator + item.name + ".apk"), BuildConfig.APPLICATION_ID + ".provider")
                            SPUtils.getInstance().put("installPath", Environment.getExternalStorageDirectory().absolutePath + File.separator + item.name + ".apk")

                        } else if (item.install_state == 0 || item.install_state == 2) {

                            val a = "$API_URL$SUF_URL_V2" + "Public/store/?service=App.downApp&id=" + item.id
                            FileDownloader.getImpl().setMaxNetworkThreadCount(1)
                            FileDownloader.getImpl().create(a)
                                    .setForceReDownload(true)
                                    .setAutoRetryTimes(2)
                                    .setPath(Environment.getExternalStorageDirectory().absolutePath + File.separator + item.name + ".apk")
                                    .setListener(LLfileDownloadListener(item)).start()
                            item.install_state = 4
                            notifyItemChanged(position)
                        }
                    }


                    if (item.task != null && item.task!!.listener == null && !isFinish)
                        item.task!!.listener = LLfileDownloadListener(item)


                }

            }
        }


        private var sepeLong4 = 0L
        private var sepeLong5 = 0L

        private fun makeBaseState(item: SoftApps) {
            if (isInstalled(item)) {
                if (isNeedUpdate(item)) {
                    if (isDownloadOK(item)) {
                        item.install_state = 3
                    } else if (item.install_state != 4 && item.install_state != 5 && item.install_state != 6) {
                        item.install_state = 2

                    }
                } else {
                    item.install_state = 1
                }
            } else {
                if (isDownloadOK(item)) {
                    item.install_state = 3
                } else if (item.install_state != 4 && item.install_state != 5 && item.install_state != 6) {
                    item.install_state = 0
                }
            }
        }


    }

    private fun MainAdapter.LLfileDownloadListener(item: SoftApps): FileDownloadListener {
        return object : FileDownloadListener() {
            override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                item.task = task
                item.task?.tag = item.id
                softNotifydatachanged(task)
            }

            override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                item.install_state = 6

            }

            override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {

                val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val result = numberFormat.format(soFarBytes.toFloat() / totalBytes.toFloat())
                try {
                    item.install_progress = ((result.toFloat() * 100).toInt())
                    softNotifydatachanged(task)
                } catch (e: Exception) {
                    item.install_progress = 0
                    softNotifydatachanged(task)
                }


            }

            override fun blockComplete(task: BaseDownloadTask?) {}

            override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {}

            override fun completed(task: BaseDownloadTask) {
                item.install_state = 3
                item.install_progress = 0
                softNotifydatachanged(task)
                item.task = null
                AppUtils.installApp(task.targetFilePath, BuildConfig.APPLICATION_ID + ".provider");
            }

            override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
//                item.install_state = 5
//                softNotifydatachanged(task)
            }

            override fun error(task: BaseDownloadTask, e: Throwable) {
                if (e.message != null && e is FileDownloadGiveUpRetryException && e.message!!.startsWith("sofar")) {
                    task.reuse()
                    task.start()
                } else if (e is SocketTimeoutException) {
                    task.reuse()
                    task.start()
                } else {
                    FileDownloader.getImpl().clear(task.id, task.targetFilePath)
                    item.install_progress = 0
                    item.install_state = 5
                    softNotifydatachanged(task)
                }


            }

            override fun warn(task: BaseDownloadTask) {

            }
        }
    }

    private fun softNotifydatachanged(task: BaseDownloadTask) {
        for (index in AppApplication.app.tmpList.indices) {
            if (AppApplication.app.tmpList[index].id == task.tag) {
                app_recyclerview.adapter.notifyItemChanged(index)
                break
            }

        }
    }


    private fun isNeedUpdate(item: SoftApps) =
            AppUtils.getAppVersionCode(item.detials[0].packageName) < item.detials[0].versionCode.toInt()

    private fun isInstalled(item: SoftApps) =
            AppUtils.isInstallApp(item.detials[0].packageName)

    private fun isDownloadOK(item: SoftApps): Boolean {
        val a = "$API_URL$SUF_URL_V2" + "Public/store/?service=App.downApp&id=" + item.id
        var b = Environment.getExternalStorageDirectory().absolutePath + File.separator + item.name + ".apk"
        if (FileUtils.isFile(b)) {
            return true
        }
        if (FileDownloader.getImpl().getStatus(a, b).toInt() == -3) {
            return true
        }
        return false
    }


}


class InitApkBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (Intent.ACTION_PACKAGE_ADDED == intent.action) {
            if (SPUtils.getInstance().getString("installPath").isEmpty())
                return
            FileUtils.deleteFile(SPUtils.getInstance().getString("installPath"))
            SPUtils.getInstance().put("installPath", "")
        }

        if (Intent.ACTION_PACKAGE_REMOVED == intent.action) {
            if (SPUtils.getInstance().getString("installPath").isEmpty())
                return
            FileUtils.deleteFile(SPUtils.getInstance().getString("installPath"))
            SPUtils.getInstance().put("installPath", "")
        }

        if (Intent.ACTION_PACKAGE_REPLACED == intent.action) {
            if (SPUtils.getInstance().getString("installPath").isEmpty())
                return
            FileUtils.deleteFile(SPUtils.getInstance().getString("installPath"))
            SPUtils.getInstance().put("installPath", "")
        }
    }


}


interface IOnClick {

    fun click(position: Int)

}

interface IOnLongClick {

    fun click(position: Int)

}

