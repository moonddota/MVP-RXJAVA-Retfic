package app.com.skylinservice.ui.device.manual

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.device.scan.ScanCodeActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_manual_input.*
import javax.inject.Inject

class ManualInputActivity : BaseActivity(), BindContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@ManualInputActivity, LoginActivity::class.java))
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

    override fun showRequestAnswer(posts: VlideNumModel) {
        super.showRequestAnswer(posts)
        dialog?.dismiss()
        if (posts.ret.toInt() == 200) {
            finish()
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    var phoenumberReady = false
    var validnumReady = false


    @Inject
    lateinit var presenter: BindContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_input)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.title = getString(R.string.activity_manuel_title)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        dialog = ProgressDialog(this)


//        manueal_activty_sn.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                phoenumberReady = p0.toString().isNotEmpty()
//                handleCommitBtnState()
//                if (phoenumberReady && !snMatches(p0.toString()))
//                    manueal_activty_sn.setError(getString(R.string.title_activity_error_phoneformat), null)
//
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//        })
        manueal_activty_actionvnum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                validnumReady = p0.toString().isNotEmpty()
                handleCommitBtnState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })



        activityComponent.inject(this)
        presenter.attachView(this)

        email_sign_in_button.setOnClickListener {
            presenter.bind(manueal_activty_actionvnum.text.toString())
        }
    }

    private fun snMatches(toString: String): Boolean {
        return true
    }

    private fun handleCommitBtnState() {
        email_sign_in_button.isEnabled = validnumReady
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.manuelscanmenu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@ManualInputActivity, ScanCodeActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
