package app.com.skylinservice.ui.device

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.*
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.data.remote.requestmodel.basedata.DeviceInfoContaner
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.BaseFragment
import app.com.skylinservice.ui.device.devicefg.IBaseListFragment
import app.com.skylinservice.ui.device.devicefg.IFLyListFragment
import app.com.skylinservice.ui.device.scan.ScanCodeActivity
import com.blankj.utilcode.util.TimeUtils
import kotlinx.android.synthetic.main.activity_device_manager.*
import kotlinx.android.synthetic.main.item_devicemanager_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DeviceManagerActivity : BaseActivity(), DeviceManagerContract.View {

    override fun showAPIExcpetion(exception: ApiException) {


        runOnUiThread {

            iflylistfg?.showAPIExcpetion(exception)
            ibaselistfg?.showAPIExcpetion(exception)
        }
    }

    var dialog: ProgressDialog? = null

    override fun setLoadingIndicatorVisible(loadingVisible: Boolean) {


        runOnUiThread {

            if (loadingVisible) {
                dialog?.setCancelable(false)
                dialog?.show()
            } else {
                dialog?.dismiss()
            }
        }


    }

    override fun showNoConnectivityError() {


        runOnUiThread {
            iflylistfg?.showNoConnecttivityError()
            ibaselistfg?.showNoConnecttivityError()
            dialog?.dismiss()
        }
    }

    override fun showUnknownError() {
//        runOnUiThread {
//            deviemanager_pullrefresh.setRefreshing(false)
//
//            ToastUtils.showShort("未知错误")
//            dialog?.dismiss()
//        }

    }

    override fun showRequestAnswer(posts: RequestBIndDevices?) {
        runOnUiThread {

            super.showRequestAnswer(posts)
            dialog?.dismiss()
            iflylistfg?.setData(posts)
            ibaselistfg?.setData(posts)

        }

    }

    override fun onResume() {
        super.onResume()
        presenter.getDevices(UserIdManager().getUserId().toString(), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == resultCode && resultCode == 104) {
            presenter.getDevices(UserIdManager().getUserId().toString(), 1)
        }
    }

    interface IOnClick {

        fun click(position: Int)

    }

    lateinit var list: MutableList<DeviceInfoContaner>

    @Inject
    lateinit var presenter: DeviceManagerContract.Presenter


    var iflylistfg: IFLyListFragment? = null
    var ibaselistfg: IBaseListFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_manager)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.title = getString(R.string.main_activity_devicemanager_title)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        activityComponent.inject(this)
        presenter.attachView(this)
        dialog = ProgressDialog(this)



        skylininditor.setRelativeViewPager(mViewPager, null)

        mViewPager.adapter = SectionsPagerAdapter(supportFragmentManager)

        skylininditor.setTitle(getString(R.string.DeviceManagerActivity_tv1), getString(R.string.DeviceManagerActivity_tv2))


//        devicemanager_reclcyview.layoutManager = LinearLayoutManager(this)
//
//
//
//        deviemanager_pullrefresh.setOnRefreshListener {
//            presenter.getDevices(UserIdManager().getUserId().toString(), 1)
//
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.devicemanager, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@DeviceManagerActivity, ScanCodeActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        internal var fgList = ArrayList<Fragment>(4)

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
            iflylistfg = IFLyListFragment()
            val data = Bundle()
            data.putString(BaseFragment.TITLE_KEY, title)
            data.putString(BaseFragment.DES_KEY, des)
            (iflylistfg as IFLyListFragment).arguments = data
            fgList.add((iflylistfg as IFLyListFragment))
        }

        private fun createIJoinFragment(title: String, des: String) {
            ibaselistfg = IBaseListFragment()
            val data = Bundle()
            data.putString(BaseFragment.TITLE_KEY, title)
            data.putString(BaseFragment.DES_KEY, des)
            (ibaselistfg as IBaseListFragment).arguments = data
            fgList.add((ibaselistfg as IBaseListFragment))
        }


        override fun getItem(position: Int): Fragment {
            return fgList[position]
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }

    inner class MainAdapter(val itemClick: IOnClick, private val items: List<DeviceInfoContaner>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_devicemanager_list, parent, false)
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
                    itemView.main_activity_devicemanager_item_fly_name.text = item.device_type_name
                    itemView.main_activity_devicemanager_item_sn.text = item._id
                    val createdate = TimeUtils.string2Date(item.device_info.create_time)
                    val binddate = TimeUtils.string2Date(item.device_info.bind_time)
                    itemView.main_activity_devicemanager_item_product_date.text = _GetYear(createdate) + "." + _GetMonth(createdate) + "." + _GetDays(createdate)
                    itemView.main_activity_devicemanager_item_fly_type.text = item.device_type_name
                    itemView.main_activity_devicemanager_item_regist_date.text = _GetYear(binddate) + "." + _GetMonth(binddate) + "." + _GetDays(binddate)
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


}