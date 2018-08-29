package app.com.skylinservice.ui.device.unbind

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_unbind.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject

class UnbindActivity : BaseActivity(), UnBindContract.View {


    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@UnbindActivity, LoginActivity::class.java))
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

    override fun showunbind(posts: VlideNumModel) {
        super.showunbind(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv23))
            setResult(104)
            finish()
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    var aa: Disposable? = null

    override fun showsendValidNum(posts: VlideNumModel) {
        super.showsendValidNum(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv24))
            launch(CommonPool) {
                Thread.sleep(1000)
                var timeTotal = 60
                aa = Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            regist_tips.visibility = View.INVISIBLE
                            if (timeTotal > 0) {
                                regit_tv_getvalid.isEnabled = false
                                regit_tv_getvalid.text = timeTotal--.toString()
                            } else {
                                regit_tv_getvalid.isEnabled = true
                                regit_tv_getvalid.text = getString(R.string.action_sign_getvalidnum)
                                aa?.dispose()

                            }
                        }, { e ->
                            regist_tips.visibility = View.VISIBLE
                            regist_tips.text = e.message
                            aa?.dispose()

                        })
            }
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }


    var phoenumberReady = false
    var validnumReady = false

    @Inject
    lateinit var presenter: UnBindContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unbind)

        dialog = ProgressDialog(this)


        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.adb_ic_ab_back_material));
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })
        toolbar.title =getString(R.string.DeviceManagerActivity_tv11)
        email_regsit_in_button.text = getString(R.string.DeviceManagerActivity_tv26)

        regit_tv_getvalid.setOnClickListener {
            presenter.sendValidNum(intent.getStringExtra("deviceId"), cellphone.text.toString())
        }


        cellphone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                phoenumberReady = p0.toString().isNotEmpty()
                handleGetValidNumTVState(p0.toString())
                handleCommitBtnState()
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


        activityComponent.inject(this)
        presenter.attachView(this)

        email_regsit_in_button.setOnClickListener {
            createDialog(getString(R.string.DeviceManagerActivity_tv11), getString(R.string.DeviceManagerActivity_tv27))
        }
    }

    private fun handleGetValidNumTVState(phone: String) {
        regit_tv_getvalid.isEnabled = phoenumberReady && cellphoneMatches(phone)
    }

    private fun cellphoneMatches(phone: String): Boolean {

//        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(1[0-9][0-9]))\\d{8}$"
//        val p = Pattern.compile(regExp)
//        val m = p.matcher(phone)
//        return m.matches()
        return phone.isNotEmpty()
    }

    private fun createDialog(title: String, content: String) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(content)
        builder.setPositiveButton(getString(R.string.activity_teammanager_btn_createteam_btn_name)) { _, _ ->
            presenter.unbind(intent.getStringExtra("deviceId"), cellphone.text.toString(), reigist_edit.text.toString())
        }
        builder.show()
    }


    private fun handleCommitBtnState() {
        email_regsit_in_button.isEnabled = phoenumberReady && validnumReady
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)

        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
        aa?.dispose()
    }
}
