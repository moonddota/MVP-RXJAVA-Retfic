package app.com.skylinservice.ui.teammanager.teamfg

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.model.TeamModel
import app.com.skylinservice.data.remote.model.TeamerModell
import app.com.skylinservice.sqlite.TeamTable
import app.com.skylinservice.ui.BaseFragment
import app.com.skylinservice.ui.teammanager.TeamManagerActivity
import kotlinx.android.synthetic.main.fragment_ijoin.*
import kotlinx.android.synthetic.main.item_ijoin_list.view.*

/**
 * Created by liuxuan on 2017/12/26.
 */
class IJoinFragment : BaseFragment() {


    class MainAdapter(val itemClick: IOnClick, private val items: List<TeamTable>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ijoin_list, parent, false)
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

                    itemView.fg_ijoin_item_title.text = item.name

                    itemView.setOnLongClickListener {
                        itemClick.click(position)
                        true
                    }

                }
            }
        }


    }

    interface IOnClick {

        fun click(position: Int)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_ijoin, null)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
//            fragment_title.text = (arguments.getString(TITLE_KEY))
//            fragment_des.text = (arguments.getString(DES_KEY))
        }
        ijoin_reclyeview.layoutManager = (LinearLayoutManager(activity))

        var team1 = listOf<TeamerModell>(TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"))
        var team2 = listOf<TeamerModell>(TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"))
        var team3 = listOf<TeamerModell>(TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"), TeamerModell(1, "aaaaa", "aaa", "nima"))


        var teams1 = TeamModel(1, "火箭队", team1)
        var teams2 = TeamModel(1, "火箭队", team2)
        var teams3 = TeamModel(1, "火箭队", team3)

        var lists = mutableListOf<TeamModel>(teams1, teams2, teams3)

//        ijoin_reclyeview.adapter = MainAdapter(object : IJoinFragment.IOnClick {
//            override fun click(position: Int) {
//                var builder = AlertDialog.Builder(activity)
//                builder.setTitle("退出团队")
//                        .setMessage("确认退出该团队吗?")
//                builder.setPositiveButton("确认") { _, _ ->
//                    lists.removeAt(position)
//                    ijoin_reclyeview.adapter.notifyDataSetChanged()
//                }
//                builder.setNegativeButton("取消")
//                { _, _ ->
//
//                }
//                builder.show()
//
//            }
//
//        }, lists)

        (activity as TeamManagerActivity).presenter.getIJoin()


        ijoin_pullrefresh.setOnRefreshListener {
            (activity as TeamManagerActivity).presenter.getIJoin()
//            lists.add(teams1)
//            ijoin_reclyeview.adapter.notifyDataSetChanged()
//            ijoin_pullrefresh.setRefreshing(false)
        }

    }


}