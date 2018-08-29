package app.com.skylinservice.ui.device.scan

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.device.manual.BindContract
import app.com.skylinservice.ui.device.manual.ManualInputActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_scan.*
import javax.inject.Inject


class ScanCodeActivity : BaseActivity(), QRCodeView.Delegate, BindContract.View {


    override fun showAPIExcpetion(exception: ApiException) {
        zxingview!!.startSpot()

        textSn = ""
        textActiveCode = ""

        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@ScanCodeActivity, LoginActivity::class.java))
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
        zxingview!!.startSpot()
        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv3))
        dialog?.dismiss()
        textSn = ""
        textActiveCode = ""

    }

    override fun showUnknownError() {
        zxingview!!.startSpot()
        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv4))
        dialog?.dismiss()
        textSn = ""
        textActiveCode = ""

    }

    override fun showRequestAnswer(posts: VlideNumModel) {
        super.showRequestAnswer(posts)
        dialog?.dismiss()
        textSn = ""
        textActiveCode = ""
        if (posts.ret.toInt() == 200) {
            zxingview!!.stopCamera()
            zxingview!!.onDestroy()
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv14))
            finish()
        } else {
            ToastUtils.showShort(posts.msg)
            zxingview!!.startSpot()

        }
    }

    @Inject
    lateinit var presenter: BindContract.Presenter

    var textSn = ""
    var textActiveCode = ""


    var isOpen = false
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        activityComponent.inject(this)
        presenter.attachView(this)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.title = getString(R.string.activity_scan_title)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        zxingview!!.setDelegate(this)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.antoscanmenu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@ScanCodeActivity, ManualInputActivity::class.java))
                setResult(110)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        zxingview!!.startCamera()
        //        zxingview.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        zxingview!!.startSpot()

    }

    override fun onStop() {
        // zxingview!!.stopCamera()
        super.onStop()

    }

    override fun onDestroy() {
        zxingview!!.stopCamera()

        zxingview!!.onDestroy()
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }

    //震动器
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(100)

    }

    override fun onScanQRCodeSuccess(result: String) {
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate()//震动
        zxingview!!.stopSpot()

        if (!TextUtils.isEmpty(result)) {


//            if (textSn.equals("")) {
//                textSn = result
//                ToastUtils.showShort("请继续扫激活码")
//                zxingview!!.startSpot()
//            } else {
//                textActiveCode = result
//                presenter.bind(textSn, textActiveCode)
//                zxingview!!.startSpot()
//            }
//
            textActiveCode = result
            presenter.bind(result)
            zxingview!!.startSpot()


        } else {
            Toast.makeText(this, getString(R.string.DeviceManagerActivity_tv15), Toast.LENGTH_SHORT).show()
            zxingview!!.startSpot()
        }
    }

    override fun onScanQRCodeOpenCameraError() {

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
    }

    fun onClick(v: View) {
        when (v.getId()) {
            R.id.start_spot -> zxingview!!.startSpot()
            R.id.stop_spot -> zxingview!!.stopSpot()
            R.id.control_flashlight -> {
                if (isOpen) {
                    isOpen = false
                    zxingview!!.closeFlashlight()
                    control_flashlight.setImageResource(R.mipmap.icon_light_off)
                    tv_flashlight.text = getString(R.string.DeviceManagerActivity_tv16)
                } else {
                    zxingview!!.openFlashlight()
                    isOpen = true
                    control_flashlight.setImageResource(R.mipmap.icon_light_on)
                    tv_flashlight.text = getString(R.string.DeviceManagerActivity_tv17)



                }
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            zxingview!!.startCamera()
            zxingview!!.startSpot()
        }
    }

    companion object {
        private val REQUEST_CODE_CAMERA = 999
        private val TAG = ScanCodeActivity::class.java.simpleName
    }
}