package app.com.skylinservice.ui.userrelative.login.regist

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import app.com.skylinservice.R
import app.com.skylinservice.R.id.password
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.TrueNameAuthedManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.main.MainActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_regist_account.*
import kotlinx.android.synthetic.main.content_regist_account.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject

class RegistAccountActivity : BaseActivity(), RigistContract.View {
    override fun showweixinConnect(post: WeixinConnectModel) {
        dialog?.dismiss()
        if (post.ret.toInt() == 200) {

            presenter.weixin_bind(post.data.unionid, post.data.code, cellphone.text.toString(), reigist_edit.text.toString())
        } else {
            ToastUtils.showShort(post.msg)
        }


    }

    override fun showweixinBind(post: WeixinBindModel) {
        dialog?.dismiss()
        when {
            post.ret.toInt() == 200 -> {
                UserIdManager().pustUserID(post.data.id.toLong())
                skylindb.update(post.data)
                SPUtils.getInstance().put("token", post.data.token)
                TrueNameAuthedManager().setFirstLanchState(post.data.relStatus.toInt())
                startActivity(Intent(this@RegistAccountActivity, MainActivity::class.java))
                finishAffinity()
                val intent = Intent()
                intent.action = "closeLogin"
                sendBroadcast(intent)
            }
            else -> {
                ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv28))
                finish()
            }
        }
    }

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@RegistAccountActivity, LoginActivity::class.java))
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

    var aa: Disposable? = null
    override fun showSend(posts: VlideNumModel) {
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


    override fun showRigst(posts: VlideRegist) {
        if (posts.ret.toInt() == 200) {
            startActivity(Intent(this@RegistAccountActivity, LoginActivity::class.java))
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }


    var phoenumberReady = false
    var validnumReady = false
    var pwdReady = false

    @Inject
    lateinit var presenter: RigistContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_regist_account)

        activityComponent.inject(this)
        presenter.attachView(this)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.adb_ic_ab_back_material));
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })
        dialog = ProgressDialog(this)

        toolbar.title = getString(R.string.TeamManagerActivity_tv29)
        email_regsit_in_button.text = getString(R.string.title_activity_regist_btn_content)
//        regist_iv_showhide.setOnClickListener { v -> troggleState(v) }

        regit_tv_getvalid.setOnClickListener {
            presenter.sendValidNum(cellphone.text.toString())
        }


        cellphone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                phoenumberReady = p0.toString().length == 11
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

        email_regsit_in_button.setOnClickListener {
            presenter.weixin_connect(intent.getStringExtra("code"))
            //            presenter.register(password.text.toString(), cellphone.text.toString(), reigist_edit.text.toString())
        }


    }

    private fun handleGetValidNumTVState(phone: String) {
        regit_tv_getvalid.isEnabled = phoenumberReady && cellphoneMatches(phone)
    }

    private fun cellphoneMatches(phone: String): Boolean {

        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(1[0-9][0-9]))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.matches()
    }

    fun isEmoji(string: String): Boolean {
        val p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        val m = p.matcher(string)
        return m.find()
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