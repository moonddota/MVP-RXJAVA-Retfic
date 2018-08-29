package app.com.skylinservice.ui.userrelative.login.changpwdbyvalid

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideForget
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import app.com.skylinservice.ui.userrelative.login.LoginContract
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.content_forgot_pwd.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject


class ForgotPwdActivity : BaseActivity(), ForgetContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@ForgotPwdActivity, LoginActivity::class.java))
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

    override fun showSendValid(posts: VlideNumModel) {
        dialog?.dismiss()

        super.showSendValid(posts)
        if (posts.ret.toInt() == 200) {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv4))
            launch(CommonPool) {
                Thread.sleep(1000)
                var timeTotal = 60
                aa = Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            forggot_tips.visibility = View.INVISIBLE
                            if (timeTotal > 0) {
                                forgo_tv_getvalid_.isEnabled = false
                                forgo_tv_getvalid_.text = timeTotal--.toString()
                            } else {
                                forgo_tv_getvalid_.isEnabled = true
                                forgo_tv_getvalid_.text = getString(R.string.action_sign_getvalidnum)
                                aa?.dispose()

                            }
                        }, { e ->
                            forggot_tips.visibility = View.VISIBLE
                            forggot_tips.text = e.message
                            aa?.dispose()

                        })
            }
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    override fun showForget(posts: VlideForget) {
        dialog?.dismiss()

        super.showForget(posts)
        if (posts.ret.toInt() == 200) {
            if (UserIdManager().getUserId().toInt() == 0) {
                finish()
            } else {
                var usertable = skylindb.getUserById(UserIdManager().getUserId())
                usertable.password = password.text.toString().trim()
                skylindb.update(usertable)
                finish()
            }


        } else {
            ToastUtils.showShort(posts.msg)
        }

    }

    var phoenumberReady = false
    var validnumReady = false
    var pwdReady = false

    @Inject
    lateinit var presenter: ForgetContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)
        activityComponent.inject(this)
        presenter.attachView(this)

        dialog = ProgressDialog(this)

        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material);
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })

        toolbar.title = getString(R.string.activity_forggot_title)
        email_regsit_in_button.text = getString(R.string.activity_forggot_btn_content)
        regist_iv_showhide.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                troggleState(v)
            }
        })
        forgo_tv_getvalid_.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                presenter.sendValidNum(cellphone.text.toString())
//                launch(CommonPool) {
//                    Thread.sleep(1000)
//                    var timeTotal = 30
//                    Observable.interval(1, TimeUnit.SECONDS)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe({
//                                forggot_tips.visibility = View.INVISIBLE
//                                if (timeTotal > 0) {
//                                    forgo_tv_getvalid_.isEnabled = false
//                                    forgo_tv_getvalid_.text = timeTotal--.toString()
//                                } else {
//                                    forgo_tv_getvalid_.isEnabled = true
//                                    forgo_tv_getvalid_.text = getString(R.string.action_sign_getvalidnum)
//                                }
//                            }, { e ->
//                                forggot_tips.visibility = View.VISIBLE
//                                forggot_tips.text = e.message
//                            })
//                }
            }

        })


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
        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var isEmoji = isEmoji(p0.toString())
                pwdReady = p0.toString().length >= 6 && !isEmoji
                if (isEmoji) {
                    password.setError(getString(R.string.SettingActivity_tv2))
                    ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv27))
                }
                handleCommitBtnState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        email_regsit_in_button.setOnClickListener {
            presenter.forget(cellphone.text.toString(), reigist_edit.text.toString(), password.text.toString())
        }

    }

    fun isEmoji(string: String): Boolean {
        val p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        val m = p.matcher(string)
        return m.find()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
        aa?.dispose()
    }

    private fun handleGetValidNumTVState(phone: String) {
        forgo_tv_getvalid_.isEnabled = phoenumberReady && cellphoneMatches(phone)
    }

    private fun cellphoneMatches(phone: String): Boolean {

        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(1[0-9][0-9]))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.matches()
    }


    private fun pwdMatches(phone: String): Boolean {

        val regExp = "^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{8,16}\$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.matches()
    }

    private fun handleCommitBtnState() {
        email_regsit_in_button.isEnabled = phoenumberReady && validnumReady && pwdReady
    }


    private fun troggleState(v: View) {
        if (v.tag.equals("true")) {
            (v as ImageView).setImageResource(R.mipmap.icon_pwd_hide)
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            v.tag = "false"
            password.setSelection(password.text.length)

        } else {
            (v as ImageView).setImageResource(R.mipmap.icon_pwd_show)
            password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            v.tag = "true"
            password.setSelection(password.text.length)

        }


    }
}
