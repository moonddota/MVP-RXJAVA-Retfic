package app.com.skylinservice.ui.teammanager.teamlist

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.model.TeamerModell
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.data.remote.requestmodel.basedata.MemberInfo
import app.com.skylinservice.data.remote.requestmodel.basedata.TeamInfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.TrueNameAuthedManager
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.manager.utils.GsonUtils.jsonToList
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.main.authed.AuthedActivity
import app.com.skylinservice.ui.set.WebActivity
import app.com.skylinservice.ui.teammanager.TeamMangerContract
import app.com.skylinservice.ui.teammanager.create.EditTeamActivity
import app.com.skylinservice.ui.teammanager.invite.InviteActivity
import app.com.skylinservice.ui.teammanager.invite.TeamerChangeActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_team_list.*
import kotlinx.android.synthetic.main.fragment_imake.*
import kotlinx.android.synthetic.main.item_team_list.view.*
import sjj.permission.PermissionCallback
import sjj.permission.model.Permission
import sjj.permission.util.PermissionUtil
import javax.inject.Inject

class TeamListActivity : BaseActivity(), TeamListContract.View {


    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@TeamListActivity, LoginActivity::class.java))
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

    override fun showUserDeleted(posts: VlideNumModel, uid: Int, tid: Long) {
        if (posts.ret.toInt() == 200) {
            ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv10))
            for (member in team1!!) {
                if (member.uid.toInt() == uid) {
                    team1!!.remove(member)
                    teamlistRecycleview.adapter.notifyDataSetChanged()
                    setResult(105)
                    break
                }
            }
        } else {
            ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv11)+":" + posts.msg)
        }

    }

    override fun showImakeMembers(posts: TeamMemeberModel, tid: Long) {
        super.showImakeMembers(posts, tid)
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            team1 = posts.data.res
            teamlistRecycleview.adapter = MainAdapter(object : IOnClick {
                override fun click(position: Int) {

                    var intent = Intent(this@TeamListActivity, TeamerChangeActivity::class.java)
                    intent.putExtra("teamlist_choosed", GsonUtils.createGsonString(team1!![position]))
                    intent.putExtra("teamlistid", teamid)

                    startActivityForResult(intent, 109)

                }

            }, object : IOnLongClick {
                override fun click(position: Int) {

                    var builder = AlertDialog.Builder(this@TeamListActivity)
                    builder.setTitle(getString(R.string.TeamManagerActivity_tv12))
                            .setMessage(getString(R.string.TeamManagerActivity_tv13))
                    builder.setPositiveButton(getString(R.string.DateChooseActivity_tv11)) { _, _ ->
                        presenter.deleteUser(team1!![position].uid.toInt(), teamid)
//                    team1!!.removeAt(position)
//                    teamlistRecycleview.adapter.notifyDataSetChanged()
                    }
                    builder.setNegativeButton(getString(R.string.cancle)) { _, _ ->
                    }
                    builder.show()

                }

            }, team1!!)
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }


    var team1: MutableList<MemberInfo>? = null

    var teamid: Long = 0
    lateinit var teamInfo: TeamInfo

    @Inject
    lateinit var presenter: TeamListContract.Presenter

    override fun onBackPressed() {

//        super.onBackPressed()
        setResult(110)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_list)
        dialog = ProgressDialog(this)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
            setResult(110)
        })
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        teamlistRecycleview

        team_btn_invite.setOnClickListener {


            if (TrueNameAuthedManager().getFirstLanchState() == 0) {

                var builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.TeamManagerActivity_tv14))
                        .setMessage(getString(R.string.TeamManagerActivity_tv15))
                builder.setPositiveButton(getString(R.string.DateChooseActivity_tv11)) { _, _ ->
                    var packageInfo: PackageInfo? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        var count = 3
                        PermissionUtil.requestPermissions(this@TeamListActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallback {
                            override fun onGranted(permissions: Permission) {
                                if (--count == 0) {
                                    var intent = Intent(this@TeamListActivity, WebActivity::class.java)

//                                            intent.putExtra(urlKey, "http://192.168.1.100?token=" + SPUtils.getInstance().getString("token"))

                                    intent.putExtra("title", getString(R.string.MainActivity_tv1))
                                    intent.putExtra(WebActivity.urlKey, "file:///android_asset/index.html?token=" + SPUtils.getInstance().getString("token"))
                                    startActivityForResult(intent, 107)
                                }

                            }

                            override fun onDenied(permissions: Permission) {
                                ToastUtils.showLong(getString(R.string.MainActivity_tv6)+":" + permissions.name)

                            }
                        })
                    } else {
                        var intent = Intent(this@TeamListActivity, WebActivity::class.java)
                        intent.putExtra("title", getString(R.string.MainActivity_tv1))

//                            intent.putExtra(urlKey, "http://192.168.1.100?token=" + SPUtils.getInstance().getString("token"))

                        intent.putExtra(WebActivity.urlKey, "file:///android_asset/index.html?token=" + SPUtils.getInstance().getString("token"))
                        startActivityForResult(intent, 107)
                    }
                }
                builder.setNegativeButton(getString(R.string.cancle))
                { _, _ ->

                }
                builder.setCancelable(false)
                builder.show()
            } else {

                var intent = Intent(this@TeamListActivity, InviteActivity::class.java)
                intent.putExtra("teamid", teamid)

                startActivityForResult(intent, 103)
            }
        }


        teamid = intent.getLongExtra("teamlistid", 0L)
        teamInfo = GsonUtils.changeGsonToBean(intent.getStringExtra("team"), TeamInfo::class.java)

        toolbar.title = teamInfo.name

        activityComponent.inject(this)
        presenter.attachView(this)

//        team1 = jsonToList(intent.getStringExtra("teamlist"), MemberInfo::class.java) ?: return
        teamlistRecycleview.layoutManager = (LinearLayoutManager(this))

        presenter.getIMakeMemers(teamid)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == requestCode && requestCode == 101) {
            setResult(101)
            finish()
            ToastUtils.showShort(R.string.TeamManagerActivity_tv10)
        } else if (requestCode == 101 && resultCode == 102) {
            teamInfo.name = data?.getStringExtra("teamName")
            teamInfo.keyTime = data?.getStringExtra("teamDay")

            ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv16))
        } else if (requestCode == 103 && resultCode == 103) {
            setResult(103)
            presenter.getIMakeMemers(teamid)

        } else if (requestCode == 109 && resultCode == 109) {
            setResult(109)
            presenter.getIMakeMemers(teamid)

        }

        if (requestCode == 107 && requestCode == resultCode) {
            setResult(107)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.editteam, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                var intent = Intent(this@TeamListActivity, EditTeamActivity::class.java)
                intent.putExtra("teamid", teamid)
                intent.putExtra("team", GsonUtils.createGsonString(teamInfo))
                startActivityForResult(intent, 101)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    inner class MainAdapter(val itemClick: IOnClick, val itemLongClick: IOnLongClick, private val items: List<MemberInfo>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindForecast(items[position], position)


        }

        override fun getItemCount(): Int {
            return items.size
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bindForecast(item: MemberInfo, position: Int) {
                with(item) {

                    itemView.teamlist_item_name.text = item.descName

                    itemView.setOnClickListener { itemClick.click(position) }
                    itemView.setOnLongClickListener {
                        itemLongClick.click(position)
                        return@setOnLongClickListener true
                    }

                }
            }
        }


    }

    interface IOnClick {

        fun click(position: Int)

    }

    interface IOnLongClick {

        fun click(position: Int)

    }
}
