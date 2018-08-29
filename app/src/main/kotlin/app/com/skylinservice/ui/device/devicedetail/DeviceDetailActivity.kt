package app.com.skylinservice.ui.device.devicedetail

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.GeneralModel
import app.com.skylinservice.data.remote.requestmodel.GeneralModelData
import app.com.skylinservice.data.remote.requestmodel.GetControlIdModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.device.scan.ScanCodeActivity
import app.com.skylinservice.ui.device.unbind.UnbindActivity
import app.com.skylinservice.ui.set.changenick.ChangeFlyNameActivity
import app.com.skylinservice.ui.set.changenick.ChangeNickNameActivity
import app.com.skylinservice.ui.softcenter.SoftCenterActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_device_detail.*
import javax.inject.Inject

class DeviceDetailActivity : BaseActivity(), DeviceDetailContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@DeviceDetailActivity, LoginActivity::class.java))
        }
    }


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

    override fun showRequestAnswer(posts: GetControlIdModel?) {
        super.showRequestAnswer(posts)
        dialog?.dismiss()
        if (posts?.ret?.toInt() == 200) {

            name.text = intent.getStringExtra("devicetypename")
//            tv_controlid.text = posts.data[0].device_info.contrl_id
            tv_creatted.text = posts.data[0].device_info.create_time
            tv_regist.text = posts.data[0].device_info.bind_time
            tv_sn.text = posts.data[0]._id
            tv_deviceName.text = intent.getStringExtra("devicename")


        } else {
            ToastUtils.showShort(posts?.msg)
        }
    }

    @Inject
    lateinit var presenter: DeviceDetailContract.Presenter

    var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_detail)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.title = getString(R.string.DeviceManagerActivity_tv5)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        activityComponent.inject(this)
        presenter.attachView(this)
        dialog = ProgressDialog(this)


        if (intent.getStringExtra("typeno").equals("101")  || intent.getStringExtra("typeno").equals("103"))
            icon_iv.setImageResource(R.mipmap.icon_device_header)
        else
            icon_iv.setImageResource(R.mipmap.icon_basetower)



        presenter.getDeviceDetial(intent.getStringExtra("deviceId"))


        gotoGujianshengji.setOnClickListener {
            if (AppUtils.isInstallApp("uav.skylin.com.upgradeforfirmware")) {
                AppUtils.launchApp("uav.skylin.com.upgradeforfirmware")
            } else {
                ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv6))
                startActivity(Intent(this, SoftCenterActivity::class.java))

            }
        }

        ll_1.setOnClickListener {
            var intent1 = Intent(this@DeviceDetailActivity, ChangeFlyNameActivity::class.java)
            intent1.putExtra("_id", intent.getStringExtra("deviceId"))
            intent1.putExtra("name", intent.getStringExtra("devicename"))

            startActivityForResult(intent1, 108)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.devicedetail, menu)
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
                var intent1 = Intent(this, UnbindActivity::class.java)
                intent1.putExtra("deviceId", intent.getStringExtra("deviceId"))
                startActivityForResult(intent1, 104)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == resultCode && resultCode == 104) {
            setResult(104, data)
            finish()
        }

        if (resultCode == 108) {
            tv_deviceName.text = data!!.getStringExtra("name")
            name.text = data!!.getStringExtra("name")
            setResult(108,data)
        }
    }



}
