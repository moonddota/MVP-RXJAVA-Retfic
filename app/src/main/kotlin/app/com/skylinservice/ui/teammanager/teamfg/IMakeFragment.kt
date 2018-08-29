package app.com.skylinservice.ui.teammanager.teamfg

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.model.TeamModel
import app.com.skylinservice.data.remote.model.TeamerModell
import app.com.skylinservice.data.remote.requestmodel.basedata.TeamInfo
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.sqlite.TeamTable
import app.com.skylinservice.ui.BaseFragment
import app.com.skylinservice.ui.teammanager.TeamManagerActivity
import app.com.skylinservice.ui.teammanager.TeamMangerContract
import app.com.skylinservice.ui.teammanager.teamlist.TeamListActivity
import kotlinx.android.synthetic.main.fragment_ijoin.*
import kotlinx.android.synthetic.main.fragment_imake.*
import kotlinx.android.synthetic.main.item_imake_list.view.*
import javax.inject.Inject

/**
 * Created by liuxuan on 2017/12/26.
 */
class IMakeFragment : BaseFragment() {


    var mContext: Context? = null

    class MainAdapter(val itemClick: IOnClick, private val items: List<TeamTable>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_imake_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindForecast(items[position], position)


        }

        override fun getItemCount(): Int {
            return items.size
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bindForecast(item: TeamTable, position: Int) {
                with(item) {

                    itemView.fg_imake_item_title.text = item.name
                    when (item.teamMembers?.size) {
                        0 -> {
                            itemView.imake_item_ll_1.visibility = View.INVISIBLE
                            itemView.imake_item_ll_2.visibility = View.INVISIBLE
                            itemView.imake_item_ll_3.visibility = View.INVISIBLE
                            itemView.imake_item_ll_4.visibility = View.INVISIBLE
                            itemView.imake_item_ll_5.visibility = View.INVISIBLE

                        }
                        1 -> {
                            itemView.imake_item_ll_1.visibility = View.VISIBLE
                            itemView.team_1.text = item.teamMembers!![0].descName
                            itemView.imake_item_ll_2.visibility = View.INVISIBLE
                            itemView.imake_item_ll_3.visibility = View.INVISIBLE
                            itemView.imake_item_ll_4.visibility = View.INVISIBLE
                            itemView.imake_item_ll_5.visibility = View.INVISIBLE

                        }
                        2 -> {
                            itemView.imake_item_ll_1.visibility = View.VISIBLE
                            itemView.imake_item_ll_2.visibility = View.VISIBLE
                            itemView.team_1.text = item.teamMembers!![0].descName
                            itemView.team_2.text = item.teamMembers!![1].descName
                            itemView.imake_item_ll_3.visibility = View.INVISIBLE
                            itemView.imake_item_ll_4.visibility = View.INVISIBLE
                            itemView.imake_item_ll_5.visibility = View.INVISIBLE

                        }
                        3 -> {
                            itemView.imake_item_ll_1.visibility = View.VISIBLE
                            itemView.imake_item_ll_2.visibility = View.VISIBLE
                            itemView.imake_item_ll_3.visibility = View.VISIBLE
                            itemView.team_1.text = item.teamMembers!![0].descName
                            itemView.team_2.text = item.teamMembers!![1].descName
                            itemView.team_3.text = item.teamMembers!![2].descName

                            itemView.imake_item_ll_4.visibility = View.INVISIBLE
                            itemView.imake_item_ll_5.visibility = View.INVISIBLE

                        }
                        4 -> {
                            itemView.imake_item_ll_1.visibility = View.VISIBLE
                            itemView.imake_item_ll_2.visibility = View.VISIBLE
                            itemView.imake_item_ll_3.visibility = View.VISIBLE
                            itemView.imake_item_ll_4.visibility = View.VISIBLE

                            itemView.team_1.text = item.teamMembers!![0].descName
                            itemView.team_2.text = item.teamMembers!![1].descName
                            itemView.team_3.text = item.teamMembers!![2].descName
                            itemView.team_4.text = item.teamMembers!![3].descName


                            itemView.imake_item_ll_5.visibility = View.INVISIBLE

                        }
                        null -> {
                            itemView.imake_item_ll_1.visibility = View.INVISIBLE
                            itemView.imake_item_ll_2.visibility = View.INVISIBLE
                            itemView.imake_item_ll_3.visibility = View.INVISIBLE
                            itemView.imake_item_ll_4.visibility = View.INVISIBLE
                        }
                        else -> {
                            itemView.imake_item_ll_1.visibility = View.VISIBLE
                            itemView.imake_item_ll_2.visibility = View.VISIBLE
                            itemView.imake_item_ll_3.visibility = View.VISIBLE
                            itemView.imake_item_ll_4.visibility = View.VISIBLE
                            itemView.imake_item_ll_5.visibility = View.VISIBLE
                            itemView.team_1.text = item.teamMembers!![0].descName
                            itemView.team_2.text = item.teamMembers!![1].descName
                            itemView.team_3.text = item.teamMembers!![2].descName
                            itemView.team_4.text = item.teamMembers!![3].descName


                        }

                    }

                    itemView.setOnClickListener { itemClick.click(position) }

                }
            }
        }


    }

    interface IOnClick {

        fun click(position: Int)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_imake, null)


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
        imake_reclyeview.layoutManager = (LinearLayoutManager(activity))

        var team1 = listOf<TeamerModell>(TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"))
        var team2 = listOf<TeamerModell>(TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"))
        var team3 = listOf<TeamerModell>(TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"))


        var teams1 = TeamModel(1, "火箭队", team1)
        var teams2 = TeamModel(1, "火箭队", team2)
        var teams3 = TeamModel(1, "火箭队", team3)

        var lists = mutableListOf<TeamModel>(teams1, teams2, teams3)


        (activity as TeamManagerActivity).presenter.getIMake()

//        imake_reclyeview.adapter = MainAdapter(object : IMakeFragment.IOnClick {
//            override fun click(position: Int) {
//                var intent = Intent(activity, TeamListActivity::class.java)
//                intent.putExtra("teamlist", GsonUtils.createGsonString(lists.get(position).teamer))
//                startActivity(intent)
//
//            }
//
//        }, lists)

        imake_pullrefresh.setOnRefreshListener {
            (activity as TeamManagerActivity).presenter.getIMake()
//            imake_pullrefresh.setRefreshing(false)
        }
    }


}