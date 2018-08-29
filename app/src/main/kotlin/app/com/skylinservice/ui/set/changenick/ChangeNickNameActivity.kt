package app.com.skylinservice.ui.set.changenick

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.set.SetContract
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_change_nick_name.*
import java.util.regex.Pattern
import javax.inject.Inject

class ChangeNickNameActivity : BaseActivity(), ChangeNickNameContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@ChangeNickNameActivity, LoginActivity::class.java))
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

    override fun showChangeUser(posts: VlideNumModel, name: String) {
        super.showChangeUser(posts, name)
        if (posts.ret.toInt() == 200) {
            var table = skylindb.getUserById(UserIdManager().getUserId())
            table.name = name
            SPUtils.getInstance().put("userName", name)
            skylindb.update(table)
            //修改昵称
            setResult(108, intent.putExtra("name", name))
            finish()

        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    @Inject
    lateinit var presenter: ChangeNickNameContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager


    var phoenumberReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_nick_name)


        activityComponent.inject(this)
        presenter.attachView(this)
        dialog = ProgressDialog(this)

        nickname_edit.text = Editable.Factory.getInstance().newEditable(skylindb.getUserById(UserIdManager().getUserId()).name)
        nickname_edit.setSelection(nickname_edit.text.toString().length)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.setting_activity_dialog_titile)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        change_nick.text = getString(R.string.activity_teammanager_btn_createteam_btn_name)

        change_nick.setOnClickListener {

            presenter.changgeUser(nickname_edit.text.toString())
        }


        nickname_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                phoenumberReady = p0.toString().length >= 1
//                handleCommitBtnState()

                var isEmoji = isEmoji(p0.toString())
                phoenumberReady = p0.toString().length >= 1 && !isEmoji
                if (isEmoji) {
                    nickname_edit.setError(getString(R.string.SettingActivity_tv2))
                    ToastUtils.showShort(getString(R.string.SettingActivity_tv3))
                }
                handleCommitBtnState()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

    fun isEmoji(string: String): Boolean {
        val p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        val m = p.matcher(string)
        return m.find()
    }


    private fun handleCommitBtnState() {
        change_nick.isEnabled = phoenumberReady
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }
}
