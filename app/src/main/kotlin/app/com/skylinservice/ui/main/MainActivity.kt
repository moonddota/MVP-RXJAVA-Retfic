package app.com.skylinservice.ui.main

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import app.com.skylinservice.AppApplication
import app.com.skylinservice.BuildConfig
import app.com.skylinservice.BuildConfig.API_URL
import app.com.skylinservice.BuildConfig.SUF_URL_V2
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.TrueNameAuthedManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.manager.customeview.CardScaleHelper
import app.com.skylinservice.manager.customeview.ReFactCardAdapter
import app.com.skylinservice.sqlite.TeamTable
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.BaseFragment
import app.com.skylinservice.ui.device.DeviceManagerActivity
import app.com.skylinservice.ui.device.scan.ScanCodeActivity
import app.com.skylinservice.ui.main.authed.AuthedActivity
import app.com.skylinservice.ui.main.mainfg.MainFragment
import app.com.skylinservice.ui.personcenter.persionstatistics.PersonStatisticesBaseActivity
import app.com.skylinservice.ui.set.SettingActivity
import app.com.skylinservice.ui.set.WebActivity
import app.com.skylinservice.ui.set.WebActivity.urlKey
import app.com.skylinservice.ui.softcenter.SoftCenterActivity
import app.com.skylinservice.ui.teammanager.TeamManagerActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.*
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.model.FileDownloadStatus
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_task_list.view.*
import retrofit2.Response
import sjj.permission.PermissionCallback
import sjj.permission.model.Permission
import sjj.permission.util.PermissionUtil
import java.io.File
import java.text.NumberFormat
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.View {


    var mTokenExpired = false
    override fun showAPIExcpetion(exception: ApiException) {
        runOnUiThread {
            if (!exception.isTokenExpried)
                ToastUtils.showShort(exception.message)
            else {
                if (!mTokenExpired) {
                    mTokenExpired = true
                    finishAffinity()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            }
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

    override fun showIJoinBack(posts: Imaketeaminfo) {


        runOnUiThread {
            if (posts.ret.toInt() == 200) {

                posts.data.data.forEach()
                { teaminfo ->
                    teaminfo.updateTime = System.currentTimeMillis()
                }

                bannerList = posts.data.data
                if (bannerList == null || bannerList.size == 0) {
                    bannerList = mutableListOf(TeamTable())
                }

                bannerrecyclerview.adapter = ReFactCardAdapter(bannerList, this, imageloader)


//                var teamid = SPUtils.getInstance().getLong("choosedteamid")

                for (index in bannerList.indices) {
                    if (bannerList.get(index).is_default == 1) {
                        SPUtils.getInstance().put("choosedteamid", bannerList.get(index).id)
                        SPUtils.getInstance().put("choosedteamname", bannerList.get(index).name)
                        bannerrecyclerview.scrollToPosition(index)
                        break
                    }

                }
                bannerrecyclerview.adapter.notifyDataSetChanged()


            } else {
                ToastUtils.showShort(posts?.msg)
            }
        }

    }

    override fun showDevicesNum(posts: RequestBIndDevices) {
        super.showDevicesNum(posts)

        runOnUiThread {
            dialog?.dismiss()

            if (posts.ret.toInt() == 200) {
                list[1][1] = bingdingDevices(posts.data[0].count.toInt())
                settingRecycle.adapter.notifyDataSetChanged()
            }
        }

    }

    override fun showBanner(posts: GetBannerModel) {
        super.showBanner(posts)
//        runOnUiThread {
//            if (posts.ret.toInt() == 200) {
//                bannerList = posts.data
//                bannerrecyclerview.adapter = ReFactCardAdapter(bannerList, this, imageloader)
//
//                bannerrecyclerview.adapter.notifyDataSetChanged()
//            } else
//                ToastUtils.showShort(posts.msg)
//        }
    }

    override fun showApps(posts: GetAPPsModel) {
        super.showApps(posts)
        runOnUiThread {
            appUnUpdates = 0
            dialog?.dismiss()
            tmpList.clear()
            for (item in posts.data[0]) {
                if (item.type.equals("app")) {
                    tmpList.add(item)
                }
                if (isInstalled(item)) {
                    //item.install_state=1
                    if (isNeedUpdate(item)) {
                        if (isDownloadOK(item)) {
                            item.install_state = 3
                        } else {
                            item.install_state = 2

                        }
                    } else {
                        item.install_state = 1
                    }
                } else {
                    if (isDownloadOK(item)) {
                        item.install_state = 3
                    } else {
                        item.install_state = 0
                    }
                }
            }

            var state = 2
            for (item in tmpList) {
                if (item.name.equals("软件中心") || item.name.equals("测量大师") || item.name.equals("天麒")) {
                    continue
                }
                if (item.install_state == 3 || item.install_state == 0) {
                    state = 0
                    break
                } else if (item.install_state == 2) {
                    state = 1
                    break
                }
            }
            for (item in tmpList) {
                if (item.name.equals("软件中心") || item.name.equals("测量大师") || item.name.equals("天麒")) {
                    continue
                }
                if (item.install_state == 3 || item.install_state == 0) {
                    appUnUpdates++
                } else if (item.install_state == 2) {
                    appUnUpdates++
                }
            }

//            tmpList
//                    .filter { it.install_state != 1 && !it.name.equals("软件中心") && !it.name.equals("测量大师") && !it.name.equals("天麒") }
//                    .forEach { appUnUpdates++ }
            list[0][1] = unUpdateApps(appUnUpdates, state)
            settingRecycle.adapter.notifyDataSetChanged()
            main_softcenter_tips.text = unUpdateApps(appUnUpdates, state)


        }

    }

    var builder: AlertDialog.Builder? = null

    override fun showLatesInfo(posts: GetAPPInfoModel) {
        super.showLatesInfo(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200 && posts.data.isNotEmpty()) {
            if (posts.data[0].versionCode.toInt() <= AppUtils.getAppVersionCode()) {
                ToastUtils.showShort(AppUtils.getAppVersionName() + getString(R.string.MainActivity_tv4))
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
                                ToastUtils.showLong(getString(R.string.MainActivity_tv6) + ":" + permissions.name)
                            } else {
                                ToastUtils.showLong(getString(R.string.MainActivity_tv6) + ":" + permissions.name)

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

    override fun showSetDefaultTeam(posts: VlideNumModel) {
        runOnUiThread {
            super.showSetDefaultTeam(posts)
            if (posts.ret.toInt() == 200) {
                presenter.getIJoin()
                SPUtils.getInstance().put("isTeamChanged", true)
            } else {
                ToastUtils.showShort(posts.msg)
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

        return FileDownloader.getImpl().getStatus(a, b) == FileDownloadStatus.completed
    }

    override fun showAuth(posts: RequestBIndDevices) {
        super.showAuth(posts)
    }


    interface IOnClick {

        fun click(position: Int)

    }


    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var imageloader: ImageLoader

    @Inject
    lateinit var skylindb: SkyLinDBManager

    lateinit var list: MutableList<MutableList<Any>>

    lateinit var bannerList: MutableList<TeamTable>


    lateinit var tmpList: MutableList<SoftApps>

    var appUnUpdates = 0

    var mCardScaleHelper: CardScaleHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityComponent.inject(this)
        presenter.attachView(this)

        tmpList = mutableListOf()

        dialog = ProgressDialog(this)

//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View) {
//                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
//            }
//        })
//        toolbar.title = getString(R.string.main_activity_title)
//        toolbar.showOverflowMenu()
//        toolbar.navigationIcon = resources.getDrawable(R.mipmap.icon_set)
//        toolbar.setBackgroundColor(resources.getColor(R.color.skylin_light_blue))
//        toolbar.setTitleTextColor(resources.getColor(R.color.primary_text))

        icon_set.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
        }

        icon_scan.setOnClickListener {
            if (TrueNameAuthedManager().getFirstLanchState() == 0) {
                ToastUtils.showShort(getString(R.string.MainActivity_tv9))
                return@setOnClickListener
            }


            var count = 3
            PermissionUtil.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
                override fun onGranted(permissions: Permission) {
                    if (--count == 0) {
                        startActivity(Intent(this@MainActivity, ScanCodeActivity::class.java))
                    }

                }

                override fun onDenied(permissions: Permission) {
                    ToastUtils.showLong(getString(R.string.MainActivity_tv6) + ":" + permissions.name)


                }
            })
        }


//        mainViewPager.adapter = SectionsPagerAdapter(supportFragmentManager)


        settingRecycle.layoutManager = GridLayoutManager(this, 2)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.skylin_light_blue)

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        var auth = if (TrueNameAuthedManager().getFirstLanchState() == 1) getString(R.string.MainActivity_tv10) else getString(R.string.MainActivity_tv11)
        list = mutableListOf(mutableListOf(getString(R.string.MainActivity_tv12), unUpdateApps(appUnUpdates, 2), R.mipmap.icon_softcenter),
                mutableListOf(getString(R.string.MainActivity_tv13), bingdingDevices(0), R.mipmap.icon_device),
                mutableListOf(getString(R.string.MainActivity_tv14), auth, R.mipmap.icon_auth),
                mutableListOf(getString(R.string.MainActivity_tv15), getString(R.string.MainActivity_tv16), R.mipmap.icon_team))

        settingRecycle.adapter = MainAdapter(object : IOnClick {
            override fun click(position: Int) {
                when (position) {
                    0 -> {
                        startActivity(Intent(this@MainActivity, SoftCenterActivity::class.java))
                    }
                    1 -> {
                        if (TrueNameAuthedManager().getFirstLanchState() == 1)
                            startActivity(Intent(this@MainActivity, DeviceManagerActivity::class.java))
                        else
                            ToastUtils.showShort(getString(R.string.MainActivity_tv9))
                    }
                    2 -> {

                        startActivity(Intent(this@MainActivity, PersonStatisticesBaseActivity::class.java))


//                        if (TrueNameAuthedManager().getFirstLanchState() == 1) {
//                            startActivity(Intent(this@MainActivity, AuthedActivity::class.java))
//                            return
//                        }
//
//                        var packageInfo: PackageInfo? = null
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                            var count = 3
//                            PermissionUtil.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
//                                override fun onGranted(permissions: Permission) {
//                                    if (--count == 0) {
//                                        var intent = Intent(this@MainActivity, WebActivity::class.java)
//
//
//                                        intent.putExtra("title", "实名认证")
//                                        intent.putExtra(urlKey, "file:///android_asset/index.html?token=" + SPUtils.getInstance().getString("token"))
//                                        startActivityForResult(intent, 107)
//                                    }
//
//                                }
//
//                                override fun onDenied(permissions: Permission) {
//                                    ToastUtils.showLong("请到设置界面打开权限:" + permissions.name)
//
//                                }
//                            })
//                        } else {
//                            if (TrueNameAuthedManager().getFirstLanchState() == 1) {
//                                ToastUtils.showShort("实名认证成功")
//                                return
//                            }
//                            var intent = Intent(this@MainActivity, WebActivity::class.java)
//                            intent.putExtra("title", "实名认证")
//
//
//                            intent.putExtra(urlKey, "file:///android_asset/index.html?token=" + SPUtils.getInstance().getString("token"))
//                            startActivityForResult(intent, 107)
//                        }


                    }
                    3 -> startActivity(Intent(this@MainActivity, TeamManagerActivity::class.java))

                }
            }

        }, list)

        main_softcenter_tips.text = unUpdateApps(0, 2)
        main_ask_tips.text = askforTime()
        main_ask_name.text = getUserName()


//        bannerList = mutableListOf(GetBannerMiddleModel("1", mutableListOf(BannerModel("", "", ""))))
        bannerList = mutableListOf(TeamTable())
        bannerrecyclerview.adapter = ReFactCardAdapter(bannerList, this, imageloader)
        bannerrecyclerview.adapter = ReFactCardAdapter(bannerList, this, imageloader)
        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        bannerrecyclerview.layoutManager = linearLayoutManager
        mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper!!.currentItemPos = 0
        mCardScaleHelper!!.attachToRecyclerView(bannerrecyclerview)


        presenter.getBindDevices(UserIdManager().getUserId().toInt(), 1)
        presenter.getUnUpdateAPPs()
//        presenter.getBanners("banner1")

        presenter.getIJoin()
        presenter.checkUpdate(10)


    }


    private fun isServerRequestOK(response: Response<GeneralModel<List<List<SoftApps>>>>): Boolean {
        return response.body() != null && response.body()!!.ret === 200 && response.body()!!.data != null && response.body()!!.data != null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 107 && requestCode == resultCode) {
            list[2][1] = if (TrueNameAuthedManager().getFirstLanchState() == 1) getString(R.string.MainActivity_tv10) else getString(R.string.MainActivity_tv11)
            settingRecycle.adapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        exit()
    }

    private var isExit = false


    private fun exit() {
        if (!isExit) {
            isExit = true
            ToastUtils.showShort(getString(R.string.MainActivity_tv17))
            // 利用handler延迟发送更改状态信息
            Handler().postDelayed({ isExit = false }, 2000)
        } else {
            FileDownloader.getImpl().unBindService()
            AppApplication.app.tmpList.clear()
            finish()
            System.exit(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getBindDevices(UserIdManager().getUserId().toInt(), 1)
        presenter.getUnUpdateAPPs()
        // presenter.getBanners("banner1")
        presenter.getIJoin()
        main_ask_tips.text = askforTime()
        main_ask_name.text = getUserName()
        list[2][1] = if (TrueNameAuthedManager().getFirstLanchState() == 1) getString(R.string.MainActivity_tv10) else getString(R.string.MainActivity_tv11)
        settingRecycle.adapter.notifyDataSetChanged()

    }

    fun unUpdateApps(nums: Int, state: Int): String {
        if (state == 0)
            return getString(R.string.MainActivity_tv18)+" " + nums+" " + getString(R.string.MainActivity_tv19)
        else if (state == 1)
            return getString(R.string.MainActivity_tv18)  + " "+nums +" "+ getString(R.string.MainActivity_tv20)
        else
            return ""

    }

    fun bingdingDevices(dsize: Int): String {
        return getString(R.string.MainActivity_tv21) +" "+ dsize+" " + getString(R.string.MainActivity_tv22)
    }

    fun askforTime(): String {
        if (TimeUtils.millis2Date(System.currentTimeMillis()).hours < 12)
            return getString(R.string.MainActivity_tv23)+","
        else if (TimeUtils.millis2Date(System.currentTimeMillis()).hours >= 12 && TimeUtils.millis2Date(System.currentTimeMillis()).hours < 18)
            return getString(R.string.MainActivity_tv24)+","
        else
            return getString(R.string.MainActivity_tv25)+","
    }

    fun getUserName(): String {
        return skylindb.getUserById(UserIdManager().getUserId()).name
    }

    fun setDefaultId(tid: String) {
        presenter.takeDufatltTeam(tid)
    }


    private fun beginDownload() {


        val dialog4 = ProgressDialog.show(this, getString(R.string.MainActivity_tv26), getString(R.string.MainActivity_tv27), false, false)


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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.test, menu)
//        return true
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> {
//                if (TrueNameAuthedManager().getFirstLanchState() == 0) {
//                    ToastUtils.showShort("请先通过实名认证")
//                    return true
//                }
//
//
//                var count = 3
//                PermissionUtil.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
//                    override fun onGranted(permissions: Permission) {
//                        if (--count == 0) {
//                            startActivity(Intent(this@MainActivity, ScanCodeActivity::class.java))
//                        }
//
//                    }
//
//                    override fun onDenied(permissions: Permission) {
//                        ToastUtils.showLong("请到设置界面打开权限:" + permissions.name)
//
//
//                    }
//                })
//
////                RxPermissions.getInstance(this@MainActivity as Context)
////                        .request(Manifest.permission.CAMERA,
////                                Manifest.permission.READ_EXTERNAL_STORAGE,
////                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
////                        .subscribe({ granted ->
////                            if (!granted) {
////                                ToastUtils.showShort("权限获取失败")
////
////                            } else {
////                                startActivity(Intent(this@MainActivity, ScanCodeActivity::class.java))
////
////                            }
////                        })
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        internal var list = ArrayList<Fragment>(4)

        init {
            for (i in 0..3) {
                when (i) {
                    0 -> {
                        createMainFragment("使用天麒服务,所有应用账户同步", "使用天麒服务登录,所有天麒app均无需再次登录,直接进入使用")
                    }
                    1 -> {
                        createMainFragment("保持应用为最新版", "新版功能,即使使用")
                    }
                    2 -> {
                        createMainFragment("绑定您的飞机", "绑定飞机保障设备安全,无授权,不起飞")
                    }
                    3 -> {
                        createMainFragment("管理作业团队", "建立队伍管理,授权使用飞机")
                    }
                }


            }
        }

        private fun createMainFragment(title: String, des: String) {
            val fragment = MainFragment()
            val data = Bundle()
            data.putString(BaseFragment.TITLE_KEY, title)
            data.putString(BaseFragment.DES_KEY, des)
            fragment.arguments = data
            list.add(fragment)
        }


        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }


    inner class MainAdapter(val itemClick: IOnClick, private val items: List<List<Any>>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_list, parent, false)
            var statusBarHeight = -1
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }
            view.layoutParams.height = ((ScreenUtils.getScreenHeight() - SizeUtils.dp2px(380.0f) - statusBarHeight) / 2.0f).toInt()
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindForecast(items[position], position)


        }

        override fun getItemCount(): Int {
            return items.size
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bindForecast(item: List<out Any>, postion: Int) {
                with(item) {
                    itemView.main_iv_content.setImageResource(item[2].toString().toInt())
                    itemView.main_tv_title.text = item[0].toString()
                    itemView.main_tv_tips.text = item[1].toString()
                    itemView.setOnClickListener { itemClick.click(postion) }

                }
            }
        }


    }
}