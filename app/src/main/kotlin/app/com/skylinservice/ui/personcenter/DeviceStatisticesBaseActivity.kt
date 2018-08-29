package app.com.skylinservice.ui.personcenter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat.finishAffinity
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.GetDeviceDetailsssModel
import app.com.skylinservice.data.remote.requestmodel.GetDeviceStatisticsModel
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.device.devicedetail.DeviceDetailActivity
import app.com.skylinservice.ui.personcenter.choose.DateChooseActivity
import app.com.skylinservice.ui.set.SetContract
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_device_statistics.*
import kotlinx.android.synthetic.main.item_statiticsbasedata.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DeviceStatisticesBaseActivity : BaseActivity(), DeviceStatisticsContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity(this@DeviceStatisticesBaseActivity)
            startActivity(Intent(this@DeviceStatisticesBaseActivity, LoginActivity::class.java))
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


    @Inject
    lateinit var presenter: DeviceStatisticsContract.Presenter


    override fun showRequestAnswer(posts: GetDeviceDetailsssModel) {
        super.showRequestAnswer(posts)
        if (posts.ret.toInt() == 200) {
            tv_devicename.text = posts.data.device_name
            tv_sn.text = posts.data.device_sn
            tv_teamname.text = posts.data.device_team
            device_some.setOnClickListener {
                var intent1 = Intent(this, DeviceDetailActivity::class.java)
                intent1.putExtra("device_fly_time", posts.data.device_fly_time)
                intent1.putExtra("device_up_down_count", posts.data.device_up_down_count)
                intent1.putExtra("deviceId", intent.getStringExtra("deviceId"))
                intent1.putExtra("typeno", intent.getStringExtra("typeno"))
                intent1.putExtra("devicetypename", intent.getStringExtra("devicetypename"))
                intent1.putExtra("devicename", intent.getStringExtra("devicename"))
                startActivityForResult(intent1, 104)
            }
        } else {
            ToastUtils.showShort(posts.msg)
        }

    }


    @SuppressLint("SetTextI18n")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 104) {
            setResult(104, data)
            finish()
            return
        }

        if (resultCode == 108) {
            setResult(104)
            if (data?.getStringExtra("name") != null)
                tv_devicename.text = data.getStringExtra("name")
        }
        if (resultCode == 101 && data != null) {
            var time = TimeUtils.millis2String(TimeUtils.string2Millis(TimeUtils.getNowString(getNowDay()) + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 1 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))

            when (data.getStringExtra("selecttime")) {
                "-0" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv5)
                    selectRuleTime = data.getStringExtra("selecttime")
                }
                time + "-" + time -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv6)
                    selectRuleTime = data.getStringExtra("selecttime")
                }
                "-7" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv7)
                    selectRuleTime = data.getStringExtra("selecttime")
                }
                "-15" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv8)
                    selectRuleTime = data.getStringExtra("selecttime")
                }
                "-30" -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv9)
                    selectRuleTime = data.getStringExtra("selecttime")
                }
                "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)) -> {
                    tv_select_rule.text = getString(R.string.DateChooseActivity_tv17)
                    selectRuleTime = data.getStringExtra("selecttime")
                }
                else -> {
                    tv_select_rule.text = data.getStringExtra("selecttime")
                    selectRuleTime = data.getStringExtra("selecttime")
                }
            }


            presenter.getBaseDataStatistics(1, 10, selectRuleTime, "", "", intent.getStringExtra("deviceId"))
        }
    }

    private fun getNowDay(): SimpleDateFormat? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf
    }

    var selectRuleTime = "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_statistics)

        activityComponent.inject(this)
        presenter.attachView(this)
        dialog = ProgressDialog(this)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.DateChooseActivity_tv40)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)


        presenter.getDeviceDetail(intent.getStringExtra("deviceId"))


        presenter.getBaseDataStatistics(1, 10, selectRuleTime, "", "", intent.getStringExtra("deviceId"))


        check_detail.setOnClickListener {
            var intent1 = Intent(this@DeviceStatisticesBaseActivity, DetailStatisticsActivity::class.java)
            intent1.putExtra("tag", 1)
            intent1.putExtra("device_sn", intent.getStringExtra("deviceId"))

            startActivity(intent1)
        }

        tv_select_rule.setOnClickListener {
            var myintent = Intent(this@DeviceStatisticesBaseActivity, DateChooseActivity::class.java)
            myintent.putExtra("choosedtime", selectRuleTime)
            startActivityForResult(myintent, 100)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }
}