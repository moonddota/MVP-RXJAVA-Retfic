package app.com.skylinservice.ui.teammanager

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.FirstLachTeamManager
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.TrueNameAuthedManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.BaseFragment
import app.com.skylinservice.ui.teammanager.create.CreateTeamActivity
import app.com.skylinservice.ui.teammanager.teamfg.IJoinFragment
import app.com.skylinservice.ui.teammanager.teamfg.IMakeFragment
import app.com.skylinservice.ui.teammanager.teamlist.TeamListActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_team_manager.*
import kotlinx.android.synthetic.main.fragment_ijoin.*
import kotlinx.android.synthetic.main.fragment_imake.*
import javax.inject.Inject


class TeamManagerActivity : BaseActivity(), TeamMangerContract.View {


    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@TeamManagerActivity, LoginActivity::class.java))
        }
    }

    override fun showImakeMembers(posts: TeamMemeberModel, tid: Long) {
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            for (teaminfo in imake!!.data.data) {
                if (teaminfo.id == tid) {
                    teaminfo.teamMembers = posts.data.res
                    imake_reclyeview.adapter.notifyDataSetChanged()
                    break
                }
            }
        } else {
            ToastUtils.showShort(posts.msg)
        }
    }


    override fun showIJoinMembers(posts: TeamMemeberModel, tid: Long) {
        dialog?.dismiss()

        if (posts.ret.toInt() == 200) {
            for (teaminfo in ijoin!!.data.data) {
                if (teaminfo.id == tid) {
                    teaminfo.teamMembers = posts.data.res
                    ijoin_reclyeview.adapter
                    break
                }
            }
        } else {
            ToastUtils.showShort(posts.msg)
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


    override fun showIJoinBack(posts: Imaketeaminfo) {
        super.showRequestAnswer(posts)
        ijoin_pullrefresh.setRefreshing(false)

        if (posts.ret.toInt() == 200) {

            posts.data.data.forEach()
            { teaminfo ->
                teaminfo.updateTime = System.currentTimeMillis()
            }
            ijoin = posts
            ijoin_reclyeview.adapter = IJoinFragment.MainAdapter(object : IJoinFragment.IOnClick {
                override fun click(position: Int) {
                    var builder = AlertDialog.Builder(this@TeamManagerActivity)
                    builder.setTitle(getString(R.string.TeamManagerActivity_tv17))
                            .setMessage(getString(R.string.TeamManagerActivity_tv18))
                    builder.setPositiveButton(getString(R.string.DateChooseActivity_tv11)) { _, _ ->
                        presenter.deleteUser(UserIdManager().getUserId().toInt(), ijoin!!.data.data[position].id)
                        // ijoin_reclyeview.adapter.notifyDataSetChanged()
                    }
                    builder.setNegativeButton(getString(R.string.cancle))
                    { _, _ ->

                    }
                    builder.show()

                }

            }, posts.data.data)
            skylindb.updateTeam(posts.data.data)
        } else {
            ToastUtils.showShort(posts?.msg)
        }
    }

    override fun showRequestAnswer(posts: Imaketeaminfo) {
        super.showRequestAnswer(posts)
        imake_pullrefresh.setRefreshing(false)

        if (posts.ret.toInt() == 200) {
            posts.data.data.forEach()
            { teaminfo ->
                teaminfo.updateTime = System.currentTimeMillis()
                presenter.getIMakeMemers(teaminfo.id)
            }
            imake = posts
            imake_reclyeview.adapter = IMakeFragment.MainAdapter(object : IMakeFragment.IOnClick {
                override fun click(position: Int) {
                    if (posts.data.data.size >= 1) {
                        if (posts.data.data[position].teamMembers != null) {
                            var intent = Intent(this@TeamManagerActivity, TeamListActivity::class.java)
                            intent.putExtra("teamlist", GsonUtils.createGsonString(posts.data.data[position].teamMembers))
                            intent.putExtra("teamlistid", posts.data.data[position].id)
                            intent.putExtra("team", GsonUtils.createGsonString(posts.data.data[position]))


                            startActivityForResult(intent, 300)
                        } else {
                            var intent = Intent(this@TeamManagerActivity, TeamListActivity::class.java)
                            intent.putExtra("teamlist", "")
                            intent.putExtra("teamlistid", posts.data.data[position].id)
                            intent.putExtra("team", GsonUtils.createGsonString(posts.data.data[position]))

                            startActivityForResult(intent, 300)
                        }
                    }

                }

            }, posts.data.data)
            skylindb.updateTeam(posts.data.data)

        } else {
            ToastUtils.showShort(posts?.msg)
        }
    }


    override fun showUserDeleted(uid: Int, tid: Long) {
        super.showUserDeleted(uid, tid)
        presenter.getIJoin()
    }

    @Inject
    lateinit var presenter: TeamMangerContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager


    var imake: Imaketeaminfo? = null
    var ijoin: Imaketeaminfo? = null


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.teammanager, menu)
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_manager)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        dialog = ProgressDialog(this)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.activity_teammanager_title)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    createDialogRight(getString(R.string.TeamManagerActivity_tv19)
                            , getString(R.string.TeamManagerActivity_tv20)+"\n" +
                            getString(R.string.TeamManagerActivity_tv21)+"\n" +
                            getString(R.string.TeamManagerActivity_tv22))
                }
            }
            true
        }

        if (FirstLachTeamManager().getFirstLanchState()) {
            createDialog(getString(R.string.TeamManagerActivity_tv19)
                    , getString(R.string.TeamManagerActivity_tv23)+"\n" +
                    getString(R.string.TeamManagerActivity_tv24)+"\n" +
                   getString(R.string.TeamManagerActivity_tv25))
            FirstLachTeamManager().setFirstLanchState(true)
        } else {
            skylininditor.setRelativeViewPager(mViewPager, teammanager_btn_create)

            mViewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
        }


        teammanager_btn_create.setOnClickListener {
            //            if (TrueNameAuthedManager().getFirstLanchState() == 0)
//                createDialog("创建团队", "你需要先通过实名认证")
//            else {
            startActivityForResult(Intent(this@TeamManagerActivity, CreateTeamActivity::class.java), 200)
            //}
        }

        activityComponent.inject(this)
        presenter.attachView(this)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100 && requestCode == 200) {
            presenter.getIMake()
            presenter.getIJoin()
        } else if (resultCode == 101 && requestCode == 300) {
            presenter.getIMake()
            presenter.getIJoin()

        } else if (resultCode == 102 && requestCode == 300) {
            presenter.getIMake()
            presenter.getIJoin()
        } else if (resultCode == 103 && requestCode == 300) {
            presenter.getIMake()
            presenter.getIJoin()
        } else if (resultCode == 105 && requestCode == 300) {
            presenter.getIMake()
            presenter.getIJoin()
        } else if (resultCode == 109 && requestCode == 300) {
            presenter.getIMake()
            presenter.getIJoin()
        }
        if (resultCode == 110) {
            presenter.getIMake()
            presenter.getIJoin()
        }

        if (resultCode == 107) {
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

    private fun createDialog(title: String, content: String) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(content)
        builder.setPositiveButton(getString(R.string.activity_teammanager_btn_createteam_btn_name)) { _, _ ->
            skylininditor.setRelativeViewPager(mViewPager, teammanager_btn_create)
            mViewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
        }
        builder.setCancelable(false)
        builder.show()
    }


    private fun createDialogRight(title: String, content: String) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(content)
        builder.setPositiveButton(getString(R.string.activity_teammanager_btn_createteam_btn_name)) { _, _ ->

        }
        builder.setCancelable(false)
        builder.show()
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        internal var list = ArrayList<Fragment>(4)

        init {
            for (i in 0..1) {
                when (i) {
                    0 -> {
                        createIMakeFragment("使用天麒服务,所有应用账户同步", "使用天麒服务登录,所有天麒app均无需再次登录,直接进入使用")
                    }
                    1 -> {
                        createIJoinFragment("保持应用为最新版", "新版功能,即使使用")
                    }
                }


            }
        }

        private fun createIMakeFragment(title: String, des: String) {
            val fragment = IMakeFragment()
            val data = Bundle()
            data.putString(BaseFragment.TITLE_KEY, title)
            data.putString(BaseFragment.DES_KEY, des)
            fragment.arguments = data
            list.add(fragment)
        }

        private fun createIJoinFragment(title: String, des: String) {
            val fragment = IJoinFragment()
            val data = Bundle()
            data.putString(BaseFragment.TITLE_KEY, title)
            data.putString(BaseFragment.DES_KEY, des)
            fragment.arguments = data
            list.add(fragment)
        }


        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }
}
