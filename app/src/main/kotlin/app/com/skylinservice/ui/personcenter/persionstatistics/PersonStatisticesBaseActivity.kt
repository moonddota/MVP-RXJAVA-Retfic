package app.com.skylinservice.ui.personcenter.persionstatistics

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat.finishAffinity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.GetDeviceStatisticsModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.TrueNameAuthedManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.main.authed.AuthedActivity
import app.com.skylinservice.ui.personcenter.DetailStatisticsActivity
import app.com.skylinservice.ui.personcenter.DeviceStatisticsContract
import app.com.skylinservice.ui.personcenter.choose.DateChooseActivity
import app.com.skylinservice.ui.set.SettingActivity
import app.com.skylinservice.ui.set.WebActivity
import app.com.skylinservice.ui.set.WebActivity.urlKey
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_person_statistics.*
import kotlinx.android.synthetic.main.item_statiticsbasedata.*
import sjj.permission.PermissionCallback
import sjj.permission.model.Permission
import sjj.permission.util.PermissionUtil
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class PersonStatisticesBaseActivity : BaseActivity(), DeviceStatisticsContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity(this@PersonStatisticesBaseActivity)
            startActivity(Intent(this@PersonStatisticesBaseActivity, LoginActivity::class.java))
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

    override fun showStatistics(posts: GetDeviceStatisticsModel) {
        super.showStatistics(posts)

        if (posts.ret.toInt() == 200) {
            total_area.text = posts.data.summary.work_area
            val numberFormat = NumberFormat.getInstance()
            numberFormat.maximumFractionDigits = 2
            val result = numberFormat.format((posts.data.summary.fly_hour.toDouble() / 3600))
            total_hour.text = result
            fly_object.text = posts.data.summary.work_num


        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    @Inject
    lateinit var presenter: DeviceStatisticsContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager

    private var selecttime = "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_statistics)

        activityComponent.inject(this)
        presenter.attachView(this)
        dialog = ProgressDialog(this)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.DateChooseActivity_tv16)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        initView()

        check_detail.setOnClickListener {
            var intent1 = Intent(this@PersonStatisticesBaseActivity, DetailStatisticsActivity::class.java)
            intent1.putExtra("tag", 2)
            startActivity(intent1)
        }
        tv_select_rule.setOnClickListener {
            var myintent = Intent(this@PersonStatisticesBaseActivity, DateChooseActivity::class.java)
            myintent.putExtra("choosedtime", selecttime)
            startActivityForResult(myintent, 100)
        }
        goAuthLL.setOnClickListener {
            truethCharge()
        }
        presenter.getBaseDataStatistics(1, 10, selecttime, "", "", "")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101 && data != null) {
            var time = TimeUtils.millis2String(TimeUtils.string2Millis(TimeUtils.getNowString(getNowDay()) + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 1 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))


            when (data.getStringExtra("selecttime")) {
                "-0" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv5)
                    selecttime = data.getStringExtra("selecttime")
                }
                time + "-" + time -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv6)
                    selecttime = data.getStringExtra("selecttime")
                }
                "-7" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv7)
                    selecttime = data.getStringExtra("selecttime")
                }
                "-15" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv8)
                    selecttime = data.getStringExtra("selecttime")
                }
                "-30" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv9)
                    selecttime = data.getStringExtra("selecttime")
                }
                "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)) -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv17)
                    selecttime = data.getStringExtra("selecttime")
                }
                else -> {
                    tv_select_rule.text = data.getStringExtra("selecttime")
                    selecttime = data.getStringExtra("selecttime")
                }
            }


            presenter.getBaseDataStatistics(1, 10, selecttime, "", "", "")
        }

        if (resultCode == 108) {
            persion_name.text = data?.getStringExtra("name")
        }

        if (requestCode == 107) {
            initView()
        }

    }

    private fun getNowDay(): SimpleDateFormat? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf
    }

    private fun truethCharge() {
        if (TrueNameAuthedManager().getFirstLanchState() == 1) {
            startActivity(Intent(this@PersonStatisticesBaseActivity, AuthedActivity::class.java))
        } else {

            var packageInfo: PackageInfo? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                var count = 3
                PermissionUtil.requestPermissions(this@PersonStatisticesBaseActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
                    override fun onGranted(permissions: Permission) {
                        if (--count == 0) {
                            var intent = Intent(this@PersonStatisticesBaseActivity, WebActivity::class.java)


                            intent.putExtra("title", getString(R.string.MainActivity_tv1))
                            intent.putExtra(urlKey, "file:///android_asset/index.html?token=" + SPUtils.getInstance().getString("token"))
                            startActivityForResult(intent, 107)
                        }

                    }

                    override fun onDenied(permissions: Permission) {
                        ToastUtils.showLong(getString(R.string.MainActivity_tv6)+":" + permissions.name)

                    }
                })
            } else {
                if (TrueNameAuthedManager().getFirstLanchState() == 1) {
                    ToastUtils.showShort(getString(R.string.DateChooseActivity_tv18))
                    return
                }
                var intent = Intent(this@PersonStatisticesBaseActivity, WebActivity::class.java)
                intent.putExtra("title", getString(R.string.MainActivity_tv1))


                intent.putExtra(urlKey, "file:///android_asset/index.html?token=" + SPUtils.getInstance().getString("token"))
                startActivityForResult(intent, 107)
            }
        }
        return
    }

    private fun initView() {
        persion_name.text = skylindb.getUserById(UserIdManager().getUserId()).name
        if (TrueNameAuthedManager().getFirstLanchState() == 1) {
            persion_isauthed.setImageResource(R.mipmap.icon_sign)
            goAuthLL.visibility = View.INVISIBLE
        } else {
            persion_isauthed.setImageResource(R.mipmap.icon_nosign)
            goAuthLL.visibility = View.VISIBLE
        }

        tv_accountnum.text = skylindb.getUserById(UserIdManager().getUserId()).id.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.personcenter, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {

                startActivityForResult(Intent(this@PersonStatisticesBaseActivity, SettingActivity::class.java), 100)


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}