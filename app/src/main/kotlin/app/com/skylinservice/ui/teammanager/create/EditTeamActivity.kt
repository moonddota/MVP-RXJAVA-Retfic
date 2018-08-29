package app.com.skylinservice.ui.teammanager.create

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.data.remote.requestmodel.basedata.TeamInfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.teammanager.edit.TeamEditContract
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.itheima.wheelpicker.WheelPicker
import kotlinx.android.synthetic.main.activity_edit_team.*
import java.util.regex.Pattern
import javax.inject.Inject

class EditTeamActivity : BaseActivity(), TeamEditContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@EditTeamActivity, LoginActivity::class.java))
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

    override fun showTeamDeleted(posts: VlideNumModel, uid: Int, tid: Int) {
        super.showTeamDeleted(posts, uid, tid)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            setResult(101)
            finish()
        } else {
            ToastUtils.showShort(posts.msg)
        }

    }

    override fun showTeamUpdate(posts: VlideNumModel, uid: Int, tid: Int) {
        super.showTeamUpdate(posts, uid, tid)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            setResult(102)
            var intent = Intent()
            intent.putExtra("teamName", team_name.text.toString())
            intent.putExtra("teamDay", team_day.text.toString())
            setResult(102, intent)
            finish()
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    var phoenumberReady = true
    var validnumReady = true

    @Inject
    lateinit var pressenter: TeamEditContract.Presenter

    var teamid: Long = 0

    lateinit var teamInfo: TeamInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material);
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.title = getString(R.string.activity_teammanager_edit_title)

        teamInfo = GsonUtils.changeGsonToBean(intent.getStringExtra("team"), TeamInfo::class.java)


        activityComponent.inject(this)
        pressenter.attachView(this)
        dialog = ProgressDialog(this)


        teamid = intent.getLongExtra("teamid", 0)

        team_name.text = Editable.Factory.getInstance().newEditable(teamInfo.name)
        team_day.text = Editable.Factory.getInstance().newEditable(teamInfo.keyTime)

        editteam_delete.setOnClickListener {
            createDialogDelete()
        }


        team_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                phoenumberReady = p0.toString().isNotEmpty()
//                handleCommitBtnState()

                var isEmoji = isEmoji(p0.toString())
                phoenumberReady = p0.toString().isNotEmpty() && !isEmoji
                if (isEmoji) {
                    team_name.setError(getString(R.string.SettingActivity_tv2))
                    ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv1))
                }
                handleCommitBtnState()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        team_day.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                validnumReady = p0.toString().length == 1
                handleCommitBtnState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        team_day.setOnFocusChangeListener { _, _ ->
            createDialog()
        }

        expred_day_til.setOnClickListener {
            createDialog()
        }
        team_day.setOnClickListener {
            KeyboardUtils.hideSoftInput(this, team_name)
            createDialog()
            team_name.isCursorVisible = false

        }
        editteam_sure.setOnClickListener {
            pressenter.updateTeam(team_name.text.toString(), team_day.text.toString().toInt(), teamid)
        }



        team_day.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                KeyboardUtils.hideSoftInput(this, team_name)

        }

        team_name.setOnClickListener {
            team_name.isCursorVisible = true
            wheelPicker.visibility = View.INVISIBLE
            showBG.visibility = View.INVISIBLE
        }


    }


    fun isEmoji(string: String): Boolean {
        val p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        val m = p.matcher(string)
        return m.find()
    }

    private fun createDialog() {
        var items = mutableListOf<String>("1", "2", "3", "4", "5", "6", "7")
//        val dialog = AlertDialog.Builder(this).setTitle("")
//                .setSingleChoiceItems(items, -1) { dialog, which ->
//                    team_day.text = Editable.Factory.getInstance().newEditable(items[which])
//                    team_day.setSelection(team_day.text?.length!! - 1)
//                    dialog.dismiss()
//                }.create()
//        dialog.show()

        wheelPicker.visibility = View.VISIBLE
        showBG.visibility = View.VISIBLE

        wheelPicker.data = items
        wheelPicker.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(p0: WheelPicker?, p1: Any?, p2: Int) {
                team_day.text = Editable.Factory.getInstance().newEditable(items[p2])
                team_day.setSelection(team_day.text?.length!! - 1)
                showBG.visibility = View.INVISIBLE

            }

        })
    }


    override fun onDestroy() {
        super.onDestroy()
        pressenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            pressenter.destroy()
        }
    }

    private fun createDialogDelete() {
        var builder = AlertDialog.Builder(this@EditTeamActivity)
        builder.setTitle(getString(R.string.TeamManagerActivity_tv2))
                .setMessage(getString(R.string.TeamManagerActivity_tv3))
        builder.setPositiveButton(getString(R.string.activity_teammanager_btn_createteam_btn_name)) { _, _ ->
            pressenter.deleteTeam(0, teamid)
        }
        builder.setNegativeButton(getString(R.string.cancle)) { _, _ ->
        }
        builder.show()

    }

    private fun handleCommitBtnState() {
        editteam_sure.isEnabled = phoenumberReady && validnumReady
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        wheelPicker.visibility = View.INVISIBLE
        showBG.visibility = View.INVISIBLE
        return super.onTouchEvent(event)
    }
}