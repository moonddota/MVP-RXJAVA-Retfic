package app.com.skylinservice.ui.personcenter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.sqlite.TeamTable
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.maclist.ReqeustAuth
import app.com.skylinservice.ui.personcenter.choose.DateChooseActivity
import app.com.skylinservice.ui.personcenter.choose.TeamChooseActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.cjj.MaterialRefreshLayout
import com.cjj.MaterialRefreshListener
import kotlinx.android.synthetic.main.activity_detail_statistics.*
import kotlinx.android.synthetic.main.item_statistics_list.view.*
import kotlinx.android.synthetic.main.item_statiticsbasedata.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailStatisticsActivity : BaseActivity(), DetailStatisticsContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        swipeRefreshLayout?.finishRefresh()
        swipeRefreshLayout?.finishRefreshLoadMore()
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            ActivityCompat.finishAffinity(this@DetailStatisticsActivity)
            startActivity(Intent(this@DetailStatisticsActivity, LoginActivity::class.java))
        }
    }

    var dialog: ProgressDialog? = null

    override fun setLoadingIndicatorVisible(loadingVisible: Boolean) {
        if (loadingVisible) {
//            dialog?.setCancelable(false)
//            dialog?.show()
        } else {
            dialog?.dismiss()
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.finishRefresh()
                swipeRefreshLayout.finishRefreshLoadMore()
            }
        }


    }

    override fun showNoConnectivityError() {
        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv3))
        dialog?.dismiss()
        swipeRefreshLayout?.finishRefresh()
        swipeRefreshLayout?.finishRefreshLoadMore()
    }

    override fun showUnknownError() {
        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv4))
        dialog?.dismiss()
        swipeRefreshLayout?.finishRefresh()
        swipeRefreshLayout?.finishRefreshLoadMore()
    }


    override fun showMaclist(posts: GetMacListModel?) {
        super.showMaclist(posts)
        if (posts?.ret?.toInt() == 200) {
            flylist = posts?.data?.aircraft_list
            tv_select_v02.setOnClickListener {

                var intent1 = Intent(this@DetailStatisticsActivity, TeamChooseActivity::class.java)
                intent1.putExtra("type", "fly")

                intent1.putExtra("gsondata", GsonUtils.createGsonString(flylist))
                startActivityForResult(intent1, 100)
            }
        } else {
            ToastUtils.showShort(posts?.msg)
        }


    }


    override fun showStatistics(posts: GetDeviceStatisticsModel) {
        super.showStatistics(posts)
        swipeRefreshLayout?.finishRefresh()
        swipeRefreshLayout?.finishRefreshLoadMore()

        if (posts.ret.toInt() == 200) {

//            choosell.visibility = View.VISIBLE
//            basedata.visibility = View.VISIBLE
//            bottomfm.visibility = View.VISIBLE

            total_area.text = posts.data.summary.work_area
            val numberFormat = NumberFormat.getInstance()
            numberFormat.maximumFractionDigits = 2
            val result = numberFormat.format((posts.data.summary.fly_hour.toDouble() / 3600))
            total_hour.text = result
            fly_object.text = posts.data.summary.work_num
            if (posts.data.list.size != 0) {
                no_hitoryll.visibility = View.INVISIBLE
                swipeRefreshLayout.visibility = View.VISIBLE
                if (index == 1) {
                    swipeRefreshLayout.setLoadMore(true)
                    workList.clear()
                    workList.addAll(posts.data.list)
                    val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
                    list_recyleview.layoutManager = linearLayoutManager
                    list_recyleview.adapter = MacListApdater(this, workList)
                } else {
                    for (workinfo in posts.data.list) {
                        if (!workList.contains(workinfo))
                            workList.add(workinfo)
                    }
                    if (list_recyleview != null && list_recyleview.adapter != null)
                        list_recyleview.adapter.notifyDataSetChanged()
                }

            } else {
                if (index == 1) {
                    workList.clear()
                    if (list_recyleview != null && list_recyleview.adapter != null)
                        list_recyleview.adapter.notifyDataSetChanged()
                    if (tv_select_v02.tag == 2) {
                        if (tv_select_time.text.toString().equals(getString(R.string.DateChooseActivity_tv17))
                                && tv_select_team.text.toString().equals(getString(R.string.DateChooseActivity_tv13))
                                && tv_select_v02.text.toString().equals(getString(R.string.DateChooseActivity_tv15))) {
                            no_hitoryll.visibility = View.VISIBLE
                            tv_clearSelect.visibility = View.INVISIBLE

                        } else {
                            no_hitoryll.visibility = View.VISIBLE
                            tv_clearSelect.visibility = View.VISIBLE


                        }
                    } else {
                        if (tv_select_time.text.toString().equals(getString(R.string.DateChooseActivity_tv17))
                                && tv_select_team.text.toString().equals(getString(R.string.DateChooseActivity_tv13))
                                && tv_select_v02.text.toString().equals(getString(R.string.DateChooseActivity_tv15))) {
                            no_hitoryll.visibility = View.VISIBLE
                            tv_clearSelect.visibility = View.INVISIBLE


                        } else {
                            no_hitoryll.visibility = View.VISIBLE
                            tv_clearSelect.visibility = View.VISIBLE


                        }
                    }
                } else {
                    swipeRefreshLayout.setLoadMore(false)

                }
            }


        } else {
            ToastUtils.showShort(posts.msg)
        }
    }

    override fun showIJoinBack(posts: Imaketeaminfo) {


        runOnUiThread {
            if (posts.ret.toInt() == 200) {

                posts.data.data.forEach()
                { teaminfo ->
                    teaminfo.updateTime = System.currentTimeMillis()
                }

                flyteamlist = posts?.data.data
                tv_select_team.setOnClickListener {
                    var intent1 = Intent(this@DetailStatisticsActivity, TeamChooseActivity::class.java)
                    intent1.putExtra("type", "team")

                    intent1.putExtra("gsondata", GsonUtils.createGsonString(flyteamlist))
                    startActivityForResult(intent1, 100)

                }


            } else {
                ToastUtils.showShort(posts?.msg)
            }
        }

    }

    override fun showAllOperatier(posts: GetALlOperaterModel?) {
        super.showAllOperatier(posts)
        if (posts?.ret?.toInt() == 200) {
            flyerlist = posts?.data
            tv_select_v02.setOnClickListener {


                if (flyerlist != null) {

                    var intent1 = Intent(this@DetailStatisticsActivity, TeamChooseActivity::class.java)
                    intent1.putExtra("type", "flyer")

                    intent1.putExtra("gsondata", GsonUtils.createGsonString(flyerlist))
                    startActivityForResult(intent1, 100)
                } else {
                    ToastUtils.showShort(getString(R.string.DateChooseActivity_tv27))
                }
            }
        } else {
            ToastUtils.showShort(posts?.msg)
        }
    }


    var flylist: List<ReqeustAuth>? = null
    var flyerlist: List<GetALlOperaterMiddelModel>? = null
    var flyteamlist: List<TeamTable>? = null

    var workList: MutableList<WorkData> = mutableListOf()


    var selectRuleTeam = ""
    var selectRuleFlyer = ""
    var selectRuleFly = ""
    var selectRuleTime = "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))

    @Inject
    lateinit var presenter: DetailStatisticsContract.Presenter

    var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_statistics)
        activityComponent.inject(this)
        presenter.attachView(this)
        dialog = ProgressDialog(this)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        swipeRefreshLayout.setMaterialRefreshListener(object : MaterialRefreshListener() {
            override fun onRefresh(materialRefreshLayout: MaterialRefreshLayout) {
                index = 1
                if (tv_select_v02.tag == 2) {
                    presenter.getBaseDataStatistics(index, 10, selectRuleTime, "", selectRuleTeam, selectRuleFly)


                } else {
                    presenter.getBaseDataStatistics(index, 10, selectRuleTime, selectRuleFlyer, selectRuleTeam, intent.getStringExtra("device_sn"))

                }
            }

            override fun onRefreshLoadMore(materialRefreshLayout: MaterialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout)
                index++
                if (tv_select_v02.tag == 2) {
                    presenter.getBaseDataStatistics(index, 10, selectRuleTime, "", selectRuleTeam, selectRuleFly)


                } else {
                    presenter.getBaseDataStatistics(index, 10, selectRuleTime, selectRuleFlyer, selectRuleTeam, intent.getStringExtra("device_sn"))

                }
            }
        })

        swipeRefreshLayout.setLoadMore(true)

        tv_select_time.setOnClickListener {
            var myintent = Intent(this@DetailStatisticsActivity, DateChooseActivity::class.java)
            myintent.putExtra("choosedtime", selectRuleTime)
            startActivityForResult(myintent, 100)

        }

        tv_select_team.setOnClickListener {
            ToastUtils.showShort(getString(R.string.DateChooseActivity_tv28))
            presenter.getIJoin()
        }

        tv_select_v02.tag = intent.getIntExtra("tag", 0)

        tv_select_v02.setOnClickListener {
            if (tv_select_v02.tag == 2) {
                ToastUtils.showShort(getString(R.string.DateChooseActivity_tv29))
                presenter.getmaclist()
            } else {
                ToastUtils.showShort(getString(R.string.DateChooseActivity_tv30))
                presenter.getAllOperater("")
            }

        }

        if (tv_select_v02.tag == 2) {
            tv_select_v02.text = getString(R.string.DateChooseActivity_tv15)
            selectRuleFly = ""
            selectRuleTeam = ""
            tv_select_team.visibility = View.VISIBLE
            hideView.visibility = View.VISIBLE
            ll_selct_time.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10.0f)
            ll_selct_team.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 5.0f)
            ll_selct_v02.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 5.0f)

            presenter.getmaclist()
            presenter.getBaseDataStatistics(index, 10, selectRuleTime, "", selectRuleTeam, selectRuleFly)


        } else {
            tv_select_v02.text = getString(R.string.DateChooseActivity_tv14)
            selectRuleFlyer = ""
            selectRuleTeam = ""
            tv_select_team.visibility = View.GONE
            hideView.visibility = View.GONE
            ll_selct_time.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10.0f)
            ll_selct_team.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f)
            ll_selct_v02.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10.0f)
            presenter.getAllOperater(intent.getStringExtra("device_sn"))
            presenter.getBaseDataStatistics(index, 10, selectRuleTime, selectRuleFlyer, selectRuleTeam, intent.getStringExtra("device_sn"))
        }
        presenter.getIJoin()




        tv_clearSelect.setOnClickListener {
            index = 1
            workList.clear()
            selectRuleTime = "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))
            selectRuleFlyer = ""
            selectRuleTeam = ""
            selectRuleFlyer = ""
            selectRuleFly = ""
            tv_select_team.text = getString(R.string.DateChooseActivity_tv13)
            if (tv_select_v02.tag == 2) {
                tv_select_v02.text = getString(R.string.DateChooseActivity_tv15)
                selectRuleFly = ""
                selectRuleTeam = ""


            } else {
                tv_select_v02.text = getString(R.string.DateChooseActivity_tv14)
                selectRuleFlyer = ""
                selectRuleTeam = ""

            }
            tv_select_time.text = getString(R.string.DateChooseActivity_tv17)
            if (tv_select_v02.tag == 2) {
                presenter.getBaseDataStatistics(index, 10, selectRuleTime, selectRuleFlyer, selectRuleTeam, selectRuleFly)


            } else {
                presenter.getBaseDataStatistics(index, 10, selectRuleTime, selectRuleFlyer, selectRuleTeam, intent.getStringExtra("device_sn"))

            }
            ToastUtils.showShort(getString(R.string.DateChooseActivity_tv31))

        }


//        choosell.visibility = View.INVISIBLE
//        basedata.visibility = View.INVISIBLE
//        bottomfm.visibility = View.INVISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }

    private fun getNowDay(): SimpleDateFormat? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100 && data != null) {
            when {
                data.getStringExtra("type") == "team" -> {
                    var team = GsonUtils.changeGsonToBean(data.getStringExtra("choose"), TeamTable::class.java)
                    if (team == null) {
                        selectRuleTeam = ""
                        tv_select_team.text = getString(R.string.DateChooseActivity_tv13)
                    } else {
                        selectRuleTeam = team.id.toString()
                        tv_select_team.text = team.name
                    }
                }
                data.getStringExtra("type") == "flyer" -> {
                    var flyer = GsonUtils.changeGsonToBean(data.getStringExtra("choose"), GetALlOperaterMiddelModel::class.java)
                    if (flyer == null) {

                        selectRuleFlyer = ""
                        tv_select_v02.text = getString(R.string.DateChooseActivity_tv14)
                    } else {
                        selectRuleFlyer = flyer.user_id
                        tv_select_v02.text = flyer.user_name
                    }

                }
                else -> {
                    var fly = GsonUtils.changeGsonToBean(data.getStringExtra("choose"), ReqeustAuth::class.java)
                    if (fly == null) {
                        selectRuleFly = ""
                        tv_select_v02.text = getString(R.string.DateChooseActivity_tv15)
                    } else {
                        selectRuleFly = fly._id.toString()
                        tv_select_v02.text = fly._id
                    }

                }
            }
        }

        if (resultCode == 101 && data != null) {

            var time = TimeUtils.millis2String(TimeUtils.string2Millis(TimeUtils.getNowString(getNowDay()) + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 1 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))
            when (data.getStringExtra("selecttime")) {

                "-0" -> {
                    tv_select_time.text = getString(R.string.DateChooseActivity_tv5)
                    selectRuleTime = data.getStringExtra("selecttime")
                    tv_select_time.textSize = 14f

                }
                time + "-" + time -> {
                    tv_select_time.text = getString(R.string.DateChooseActivity_tv6)
                    selectRuleTime = data.getStringExtra("selecttime")
                    tv_select_time.textSize = 14f

                }
                "-7" -> {
                    tv_select_time.text = getString(R.string.DateChooseActivity_tv7)
                    selectRuleTime = data.getStringExtra("selecttime")
                    tv_select_time.textSize = 14f

                }
                "-15" -> {
                    tv_select_time.text = getString(R.string.DateChooseActivity_tv8)
                    selectRuleTime = data.getStringExtra("selecttime")
                    tv_select_time.textSize = 14f

                }
                "-30" -> {
                    tv_select_time.text = getString(R.string.DateChooseActivity_tv9)
                    selectRuleTime = data.getStringExtra("selecttime")
                }
                "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)) -> {
                    tv_select_time.text = getString(R.string.DateChooseActivity_tv17)
                    selectRuleTime = data.getStringExtra("selecttime")
                    tv_select_time.textSize = 14f

                }
                else -> {
                    tv_select_time.text = data.getStringExtra("selecttime")
                    selectRuleTime = data.getStringExtra("selecttime")
                    tv_select_time.textSize = 12f
                }
            }
        }
        if (tv_select_v02.tag == 2) {
            index = 1
            presenter.getBaseDataStatistics(index, 10, selectRuleTime, "", selectRuleTeam, selectRuleFly)


        } else {
            index = 1
            presenter.getBaseDataStatistics(index, 10, selectRuleTime, selectRuleFlyer, selectRuleTeam, intent.getStringExtra("device_sn"))

        }
    }

    inner class MacListApdater(private val mContext: Context, private val taskList: List<WorkData>?) : RecyclerView.Adapter<MacListApdater.NormalTextViewHolder>() {
        private val mLayoutInflater: LayoutInflater


        init {
            mLayoutInflater = LayoutInflater.from(mContext)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalTextViewHolder {
            return if (tv_select_v02.tag == 2) {
                NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_statistics_list, parent, false))

            } else {
                NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_statistics_list_3, parent, false))

            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: NormalTextViewHolder, position: Int) {
            if (position % 2 == 0) {
                holder.itemView.mac_ll.setBackgroundResource(R.color.white)
            } else {
                holder.itemView.mac_ll.setBackgroundResource(R.color.light_graey)

            }
            holder.mylist = taskList
            holder.postion = position
            TimeUtils.string2Date(taskList?.get(position)?.work_at).day
            holder.tv_montn.text = (TimeUtils.string2Date(taskList?.get(position)?.work_at).month.toInt() + 1).toString()

            if (TimeUtils.string2Date(taskList?.get(position)?.work_at).date.toString().length == 1)
                holder.tv_day.text = "0" + TimeUtils.string2Date(taskList?.get(position)?.work_at).date.toString()
            else
                holder.tv_day.text = TimeUtils.string2Date(taskList?.get(position)?.work_at).date.toString()


            if (TimeUtils.string2Date(taskList?.get(position)?.work_at).minutes.toString().length == 1 && TimeUtils.string2Date(taskList?.get(position)?.work_at).seconds.toString().length == 1) {
                holder.tv_time.text = TimeUtils.string2Date(taskList?.get(position)?.work_at).hours.toString() + ":0" + TimeUtils.string2Date(taskList?.get(position)?.work_at).minutes.toString() + ":0" + TimeUtils.string2Date(taskList?.get(position)?.work_at).seconds.toString()

            } else if (TimeUtils.string2Date(taskList?.get(position)?.work_at).seconds.toString().length == 1) {
                holder.tv_time.text = TimeUtils.string2Date(taskList?.get(position)?.work_at).hours.toString() + ":" + TimeUtils.string2Date(taskList?.get(position)?.work_at).minutes.toString() + ":0" + TimeUtils.string2Date(taskList?.get(position)?.work_at).seconds.toString()

            } else if (TimeUtils.string2Date(taskList?.get(position)?.work_at).minutes.toString().length == 1) {
                holder.tv_time.text = TimeUtils.string2Date(taskList?.get(position)?.work_at).hours.toString() + ":0" + TimeUtils.string2Date(taskList?.get(position)?.work_at).minutes.toString() + ":0" + TimeUtils.string2Date(taskList?.get(position)?.work_at).seconds.toString()

            } else {
                holder.tv_time.text = TimeUtils.string2Date(taskList?.get(position)?.work_at).hours.toString() + ":" + TimeUtils.string2Date(taskList?.get(position)?.work_at).minutes.toString() + ":" + TimeUtils.string2Date(taskList?.get(position)?.work_at).seconds.toString()

            }
            val numberFormat = NumberFormat.getInstance()
            numberFormat.maximumFractionDigits = 2
            val result = numberFormat.format(taskList?.get(position)?.work_time?.toFloat()!! / 60.toFloat())
            try {
                holder.tv_flytime.text = ((result.toFloat()).toInt()).toString()+" "+
                        getString(R.string.DateChooseActivity_tv32)+" "+
                        (taskList?.get(position)?.work_time.toFloat()!! - ((result.toFloat()).toInt() * 60)).toInt()+
                        getString(R.string.DateChooseActivity_tv33)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            holder.tv_area.text = taskList?.get(position)?.work_area + getString(R.string.DateChooseActivity_tv35)
            holder.tv_location.text = taskList?.get(position)?.work_address
            holder.tv_team.text = taskList?.get(position)?.work_team
            if (tv_select_v02.tag == 2)
                holder.tv_flyname.text = taskList?.get(position)?.work_device
            else
                holder.tv_team.text = taskList?.get(position)?.work_user

        }

        override fun getItemCount(): Int {
            return taskList!!.size
        }


        inner class NormalTextViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var mylist: List<WorkData>? = null

            internal var tv_montn: TextView = view.findViewById(R.id.tv_montn)
            internal var tv_day: TextView = view.findViewById(R.id.tv_day)
            internal var tv_time: TextView = view.findViewById(R.id.tv_time)
            internal var tv_flytime: TextView = view.findViewById(R.id.tv_flytime)
            internal var tv_area: TextView = view.findViewById(R.id.tv_area)
            internal var tv_location: TextView = view.findViewById(R.id.tv_location)
            internal var tv_team: TextView = view.findViewById(R.id.tv_team)
            internal var tv_flyname: TextView = view.findViewById(R.id.tv_flyname)
            internal var postion: Int = 0


        }
    }
}
