package app.com.skylinservice.ui.maclist

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_mac_list.*
import javax.inject.Inject

class MacLIstActivity : BaseActivity(), MacContract.View {

    override fun showAPIExcpetion(exception: ApiException) {
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            finishAffinity()
            startActivity(Intent(this@MacLIstActivity, LoginActivity::class.java))
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

    override fun showMaclist(posts: GetMacListModel?) {
        super.showMaclist(posts)
        if (posts?.ret?.toInt() == 200) {
            if (posts?.data?.aircraft_list.size == 0) {
                ToastUtils.showLong(getString(R.string.MacLIstActivity_tv2))
            } else {
                val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
                recycleView.layoutManager = linearLayoutManager
                recycleView.adapter = MacListApdater(this, posts.data.aircraft_list)
            }
        } else {
            ToastUtils.showLong(posts?.msg)
        }
    }

    @Inject
    lateinit var presenter: MacContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        presenter.attachView(this)

        setContentView(R.layout.activity_mac_list)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.MacLIstActivity_tv1)

        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        presenter.getmaclist(intent.getStringExtra("team_id"))


    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }


    inner class MacListApdater(private val mContext: Context, private val taskList: List<ReqeustAuth>?) : RecyclerView.Adapter<MacListApdater.NormalTextViewHolder>() {
        private val mLayoutInflater: LayoutInflater


        init {
            mLayoutInflater = LayoutInflater.from(mContext)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalTextViewHolder {
            return NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_mac_list, parent, false))
        }

        override fun onBindViewHolder(holder: NormalTextViewHolder, position: Int) {
            holder.mylist = taskList
            holder.postion = position
            holder.device_name.text = taskList?.get(position)?.device_name
            holder.device_sn.text = taskList?.get(position)?._id


            holder.macLL.setOnClickListener {

            }


        }

        override fun getItemCount(): Int {
            return taskList!!.size
        }


        inner class NormalTextViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var mylist: List<ReqeustAuth>? = null

            internal var macLL: LinearLayout
            internal var device_name: TextView

            internal var device_sn: TextView


            internal var postion: Int = 0


            init {
                macLL = view.findViewById(R.id.mac_ll)

                device_name = view.findViewById(R.id.device_name)
                device_sn = view.findViewById(R.id.device_sn)
            }
        }
    }
}
