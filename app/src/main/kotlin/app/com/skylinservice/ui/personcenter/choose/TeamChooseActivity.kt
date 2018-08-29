package app.com.skylinservice.ui.personcenter.choose

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.GetALlOperaterMiddelModel
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.sqlite.TeamTable
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.maclist.ReqeustAuth
import kotlinx.android.synthetic.main.activity_team_choose.*

class TeamChooseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_choose)
        setContentView(R.layout.activity_mac_list)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.DateChooseActivity_tv12)

        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        if (intent.getStringExtra("type") == "team") {
            var list = GsonUtils.jsonToList(intent.getStringExtra("gsondata"), TeamTable::class.java)
            list.add(0, TeamTable())
            val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = linearLayoutManager
            recycleView.adapter = MacListApdater(this, list)
        } else if (intent.getStringExtra("type") == "flyer") {
            var list = GsonUtils.jsonToList(intent.getStringExtra("gsondata"), GetALlOperaterMiddelModel::class.java)
            list.add(0, GetALlOperaterMiddelModel("", "", ""))
            val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = linearLayoutManager
            recycleView.adapter = MacListApdater(this, list)
        } else {
            var list = GsonUtils.jsonToList(intent.getStringExtra("gsondata"), ReqeustAuth::class.java)
            list.add(0, ReqeustAuth())
            val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = linearLayoutManager
            recycleView.adapter = MacListApdater(this, list)
        }

    }


    inner class MacListApdater(private val mContext: Context, private val taskList: List<Any>?) : RecyclerView.Adapter<MacListApdater.NormalTextViewHolder>() {
        private val mLayoutInflater: LayoutInflater


        init {
            mLayoutInflater = LayoutInflater.from(mContext)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalTextViewHolder {
            return NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_team_choose_list, parent, false))
        }

        override fun onBindViewHolder(holder: NormalTextViewHolder, position: Int) {
            holder.mylist = taskList
            if (taskList?.get(position) is TeamTable) {
                if ((taskList?.get(position) as TeamTable).id == 0L) {
                    holder.teamname.text = getString(R.string.DateChooseActivity_tv13)

                } else {

                    holder.teamname.text = (taskList?.get(position) as TeamTable).name
                }
            } else if (taskList?.get(position) is GetALlOperaterMiddelModel) {
                if ((taskList?.get(position) as GetALlOperaterMiddelModel).user_id == null || (taskList?.get(position) as GetALlOperaterMiddelModel).user_id == "") {
                    holder.teamname.text = getString(R.string.DateChooseActivity_tv14)

                } else {

                    holder.teamname.text = (taskList?.get(position) as GetALlOperaterMiddelModel).user_name
                }


            } else {
                if ((taskList?.get(position) as ReqeustAuth)._id == null || (taskList?.get(position) as ReqeustAuth)._id.equals("")) {
                    holder.teamname.text = getString(R.string.DateChooseActivity_tv15)

                } else {

                    holder.teamname.text = (taskList?.get(position) as ReqeustAuth)._id
                }
            }
            holder.itemView.setOnClickListener {
                if (position == 0) {
                    var intent1 = Intent()
                    intent1.putExtra("type", intent.getStringExtra("type"))
                    intent1.putExtra("choose", "")
                    setResult(100, intent1)
                    finish()
                } else {
                    var intent1 = Intent()
                    intent1.putExtra("type", intent.getStringExtra("type"))
                    intent1.putExtra("choose", GsonUtils.createGsonString(taskList[position]))
                    setResult(100, intent1)
                    finish()
                }
            }


        }

        override fun getItemCount(): Int {
            return taskList!!.size
        }


        inner class NormalTextViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var mylist: List<Any>? = null

            internal var teamname: TextView = view.findViewById(R.id.teamname)

        }
    }
}
