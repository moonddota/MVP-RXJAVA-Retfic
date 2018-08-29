package app.com.skylinservice.ui.userrelative.login.changephone

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import app.com.skylinservice.ui.userrelative.login.changpwdbyvalid.ForgetContract
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_change_phone.*
import kotlinx.android.synthetic.main.content_change_phone.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject


class ChangePhoneActivity : BaseActivity(), ChangePhoneContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@ChangePhoneActivity, LoginActivity::class.java))
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

    override fun sendNum(posts: VlideNumModel) {
        super.sendNum(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv24))
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


    var bb: Disposable? = null

    override fun sendOrigalNum(posts: VlideNumModel) {
        super.sendOrigalNum(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv24))
            launch(CommonPool) {
                Thread.sleep(1000)
                var timeTotal = 60
                bb = Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            forggot_tips.visibility = View.INVISIBLE
                            if (timeTotal > 0) {
                                origital_tv_getvalid_.isEnabled = false
                                origital_tv_getvalid_.text = timeTotal--.toString()
                            } else {
                                origital_tv_getvalid_.isEnabled = true
                                origital_tv_getvalid_.text = getString(R.string.action_sign_getvalidnum)
                                bb?.dispose()

                            }
                        }, { e ->
                            forggot_tips.visibility = View.VISIBLE
                            forggot_tips.text = e.message
                            bb?.dispose()

                        })
            }
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    override fun changePhone(posts: VlideNumModel) {
        super.changePhone(posts)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            var usertable = skylindb.getUserById(UserIdManager().getUserId())
            usertable.telphone = cellphone.text.toString().trim()
            skylindb.update(usertable)
            finish()

        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    var phoenumberReady = false
    var validnumReady = false
    var origialvalidReady = false

    @Inject
    lateinit var presenter: ChangePhoneContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.adb_ic_ab_back_material));
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })
        dialog = ProgressDialog(this)


        activityComponent.inject(this)
        presenter.attachView(this)

        toolbar.title = getString(R.string.activity_changephone_title)
        email_regsit_in_button.text = getString(R.string.activity_forggot_btn_content)
//        regist_iv_showhide.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View) {
//                troggleState(v)
//            }
//        })
        forgo_tv_getvalid_.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                launch(CommonPool) {
                    Thread.sleep(1000)
                    var timeTotal = 60
                    Observable.interval(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                forggot_tips.visibility = View.INVISIBLE
                                if (timeTotal > 0) {
                                    forgo_tv_getvalid_.isEnabled = false
                                    forgo_tv_getvalid_.text = timeTotal--.toString()
                                } else {
                                    forgo_tv_getvalid_.isEnabled = true
                                    forgo_tv_getvalid_.text = getString(R.string.action_sign_getvalidnum)
                                }
                            }, { e ->
                                forggot_tips.visibility = View.VISIBLE
                                forggot_tips.text = e.message
                            })
                }
            }

        })

        origital_tv_getvalid_.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                launch(CommonPool) {
                    Thread.sleep(1000)
                    var timeTotal = 60
                    Observable.interval(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                forggot_tips.visibility = View.INVISIBLE
                                if (timeTotal > 0) {
                                    forgo_tv_getvalid_.isEnabled = false
                                    forgo_tv_getvalid_.text = timeTotal--.toString()
                                } else {
                                    forgo_tv_getvalid_.isEnabled = true
                                    forgo_tv_getvalid_.text = getString(R.string.action_sign_getvalidnum)
                                }
                            }, { e ->
                                forggot_tips.visibility = View.VISIBLE
                                forggot_tips.text = e.message
                            })
                }
            }

        })


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
        origital_valid_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                origialvalidReady = p0.toString().length == 4
                handleCommitBtnState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        origital_tv_getvalid_.setOnClickListener {
            presenter.sendOrigal()

        }

        forgo_tv_getvalid_.setOnClickListener {
            presenter.sendValidNum(cellphone.text.toString())
        }

        email_regsit_in_button.setOnClickListener {
            presenter.changePone(origital_valid_edit.text.toString(), reigist_edit.text.toString(), cellphone.text.toString())
        }


        textPhone.text = getString(R.string.TeamManagerActivity_tv26) + skylindb.getUserById(UserIdManager().getUserId()).telphone


    }

    private fun handleGetValidNumTVState(phone: String) {
        forgo_tv_getvalid_.isEnabled = phoenumberReady && cellphoneMatches(phone)
    }

    private fun cellphoneMatches(phone: String): Boolean {

//        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(1[0-9][0-9]))\\d{8}$"
//        val p = Pattern.compile(regExp)
//        val m = p.matcher(phone)
//        return m.matches()
        return phone.isNotEmpty()
    }


    private fun pwdMatches(phone: String): Boolean {

        val regExp = "^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{8,16}\$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.matches()
    }

    private fun handleCommitBtnState() {
        email_regsit_in_button.isEnabled = phoenumberReady && validnumReady && origialvalidReady
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }

        aa?.dispose()
        bb?.dispose()
    }


}
