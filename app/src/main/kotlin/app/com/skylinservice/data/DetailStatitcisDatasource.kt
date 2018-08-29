package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface DetailStatitcisDatasource {
    fun getDeviceStatistics(page: Int, pageSize: Int, statisticTime: String, userid: String, teamnid: String, devicesn: String): Observable<GetDeviceStatisticsModel>
    fun getDeviceDetail(sn: String): Observable<GetDeviceDetailsssModel>
    fun getIJoin(): Observable<Imaketeaminfo>
    fun getMaclist(): Observable<GetMacListModel>

    fun getAllOperaqter(device_sn: String): Observable<GetALlOperaterModel>
}

class DetailStatitcisRepository(private val api: Api) : DetailStatitcisDatasource {
    override fun getAllOperaqter(device_sn: String): Observable<GetALlOperaterModel> {
        return api.getDetialAllOperater(SPUtils.getInstance().getString("token"), device_sn)

    }

    override fun getDeviceDetail(sn: String): Observable<GetDeviceDetailsssModel> {
        return api.getDeviceDetailssss(SPUtils.getInstance().getString("token"), sn)
    }

    override fun getDeviceStatistics(page: Int, pageSize: Int, statisticTime: String, userid: String, teamnid: String, devicesn: String): Observable<GetDeviceStatisticsModel> {
//        if (teamnid == "")
//            return api.getDeviceStatisticsNoteam(SPUtils.getInstance().getString("token"), page, pageSize, statisticTime, userid, devicesn)
//        else
//            return api.getDeviceStatistics(SPUtils.getInstance().getString("token"), page, pageSize, statisticTime, userid, teamnid, devicesn)
        var map = mutableMapOf<String, String>()
        map["page"] = page.toString()
        map["page_size"] = pageSize.toString()
        if (statisticTime != "")
            map["statistic_time"] = statisticTime
        if (userid != "")
            map["work_user_id"] = userid
        if (teamnid != "")
            map["work_team_id"] = teamnid
        if (devicesn != "")
            map["device_sn"] = devicesn

        return api.getDeviceStatisticsPost(SPUtils.getInstance().getString("token"), map)
    }

    override fun getIJoin(): Observable<Imaketeaminfo> {
        return api.ijoinTeamInfo(SPUtils.getInstance().getString("token"))
    }

    override fun getMaclist(): Observable<GetMacListModel> {
        return api.getMaclistuser(SPUtils.getInstance().getString("token"))
    }

}