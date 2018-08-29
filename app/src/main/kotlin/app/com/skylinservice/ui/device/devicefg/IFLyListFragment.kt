package app.com.skylinservice.ui.device.devicefg

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.data.remote.requestmodel.basedata.DeviceInfoContaner
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseFragment
import app.com.skylinservice.ui.device.DeviceManagerActivity
import app.com.skylinservice.ui.device.devicedetail.DeviceDetailActivity
import app.com.skylinservice.ui.device.unbind.UnbindActivity
import app.com.skylinservice.ui.personcenter.DeviceStatisticesBaseActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_ifly_list.*
import kotlinx.android.synthetic.main.item_newdevicemanager_list.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [IFLyListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [IFLyListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IFLyListFragment : BaseFragment() {

    var mContext: Context? = null

    lateinit var list: MutableList<DeviceInfoContaner>


    inner class MainAdapter(val itemClick: IOnClick, private val items: List<DeviceInfoContaner>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_newdevicemanager_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindForecast(items[position], position)


        }

        override fun getItemCount(): Int {
            return items.size
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            @SuppressLint("SetTextI18n")
            fun bindForecast(item: DeviceInfoContaner, position: Int) {
                with(item) {
                    itemView.main_activity_devicemanager_item_fly_name.text = item.device_info.device_name
                    itemView.main_activity_devicemanager_item_sn.text = item._id + "(sn)"
                    val createdate = TimeUtils.string2Date(item.device_info.create_time)
                    val binddate = TimeUtils.string2Date(item.device_info.bind_time)
//                    itemView.main_activity_devicemanager_item_product_date.text = _GetYear(createdate) + "." + _GetMonth(createdate) + "." + _GetDays(createdate)
//                    itemView.main_activity_devicemanager_item_fly_type.text = item.device_type_name
//                    itemView.main_activity_devicemanager_item_regist_date.text = _GetYear(binddate) + "." + _GetMonth(binddate) + "." + _GetDays(binddate)
                    itemView.setOnClickListener { itemClick.click(position) }

                }
            }
        }

        private fun _GetYear(date: Date): String {
            val sdf = SimpleDateFormat("yyyy")
            return sdf.format(date)
        }

        private fun _GetMonth(date: Date): String {
            val sdf = SimpleDateFormat("MM")
            return sdf.format(date)
        }

        private fun _GetDays(date: Date): String {
            val sdf = SimpleDateFormat("dd")
            return sdf.format(date)
        }

    }

    interface IOnClick {

        fun click(position: Int)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_ifly_list, null)


        return view
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mContext = activity as Context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
//            fragment_title.text = (arguments.getString(TITLE_KEY))
//            fragment_des.text = (arguments.getString(DES_KEY))
        }
        devicemanager_reclcyview.layoutManager = LinearLayoutManager(activity)



        deviemanager_pullrefresh.setOnRefreshListener {
            (activity as DeviceManagerActivity).presenter.getDevices(UserIdManager().getUserId().toString(), 1)

        }
        list = mutableListOf()
    }


    fun setData(posts: RequestBIndDevices?) {
        deviemanager_pullrefresh.setRefreshing(false)


        deviemanager_pullrefresh.setRefreshing(false)
        if (posts?.ret?.toInt() == 200) {
            list.clear()
            var tmplist = posts.data[0].data as MutableList<DeviceInfoContaner>
            for (devie in tmplist) {
                if (devie.type_no.equals("101")  || devie.type_no.equals("103")  )
                    list.add(devie)
            }
            devicemanager_reclcyview.adapter = MainAdapter(object : IOnClick {
                override fun click(position: Int) {
                    var intent = Intent(activity, DeviceStatisticesBaseActivity::class.java)
                    intent.putExtra("deviceId", list[position]._id)
                    intent.putExtra("typeno", list[position].type_no)
                    intent.putExtra("devicetypename", list[position].device_type_name)
                    intent.putExtra("devicename", list[position].device_info.device_name)
                    activity?.startActivityForResult(intent, 104)
                }

            }, list)
        } else {
            ToastUtils.showShort(posts?.msg)
        }
    }

    fun showAPIExcpetion(exception: ApiException) {
        deviemanager_pullrefresh.setRefreshing(false)
        if (!exception.isTokenExpried)
            ToastUtils.showShort(exception.message)
        else {
            activity?.finishAffinity()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    fun showNoConnecttivityError() {
        deviemanager_pullrefresh.setRefreshing(false)

        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv3))
    }

}// Required empty public constructor
