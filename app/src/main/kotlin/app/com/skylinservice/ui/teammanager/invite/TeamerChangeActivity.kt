package app.com.skylinservice.ui.teammanager.invite

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.model.TeamerModell
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.data.remote.requestmodel.basedata.MemberInfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.teammanager.teamlist.TeamListContract
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_teamer_change.*
import java.util.regex.Pattern
import javax.inject.Inject

class TeamerChangeActivity : BaseActivity(), TeamChangeContract.View {


    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@TeamerChangeActivity, LoginActivity::class.java))
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

    override fun showChange(post: VlideNumModel) {
        super.showChange(post)
        if (post.ret.toInt() == 200) {
            //刷新界面
            setResult(109)
            finish()
        } else {
            ToastUtils.showShort(post.msg)

        }
    }

    var phoenumberReady = true
    var validnumReady = false

    var teamid = 0L

    var uid = 0

    @Inject
    lateinit var presenter: TeamChangeContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_change)
        activityComponent.inject(this)
        presenter.attachView(this)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.activity_teammanager_teamchange_title)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)


        uid = GsonUtils.changeGsonToBean(intent.getStringExtra("teamlist_choosed"), MemberInfo::class.java).uid.toInt()

        invite_skylin_account.text = Editable.Factory.getInstance()
                .newEditable(GsonUtils.changeGsonToBean(intent.getStringExtra("teamlist_choosed"), MemberInfo::class.java).uid)
        invite_skylin_beizhu.text = Editable.Factory.getInstance()
                .newEditable(GsonUtils.changeGsonToBean(intent.getStringExtra("teamlist_choosed"), MemberInfo::class.java).descName)

        invite_skylin_beizhu.setSelection(invite_skylin_beizhu.text.toString().length)

        teamid = intent.getLongExtra("teamlistid", 0L)

//        invite_skylin_account.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                phoenumberReady = p0.toString().isNotEmpty()
//                handleCommitBtnState()
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
        invite_skylin_beizhu.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                validnumReady = p0.toString().length >= 1
//                if (p0.toString().length < 1)
//                    invite_skylin_beizhu.setError("备注名不能为空")
//                handleCommitBtnState()

                var isEmoji = isEmoji(p0.toString())
                validnumReady = p0.toString().isNotEmpty() && !isEmoji
                if (isEmoji) {
                    invite_skylin_beizhu.setError(getString(R.string.SettingActivity_tv2))
                    ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv7))
                }
                if (p0.toString().length < 1)
                    invite_skylin_beizhu.setError(getString(R.string.TeamManagerActivity_tv9))
                handleCommitBtnState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        email_sign_in_button.setOnClickListener {
            presenter.change(teamid, uid, invite_skylin_beizhu.text.toString())
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
    }


    private fun handleCommitBtnState() {
        email_sign_in_button.isEnabled = phoenumberReady && validnumReady
    }
}
