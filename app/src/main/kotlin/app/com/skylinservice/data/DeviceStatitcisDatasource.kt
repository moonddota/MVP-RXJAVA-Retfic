package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.GetDeviceDetailsssModel
import app.com.skylinservice.data.remote.requestmodel.GetDeviceStatisticsModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface DeviceStatitcisDatasource {
    fun getDeviceStatistics(page: Int, pageSize: Int, statisticTime: String, userid: String, teamnid: String, devicesn: String): Observable<GetDeviceStatisticsModel>
    fun getDeviceDetail(sn: String): Observable<GetDeviceDetailsssModel>
}

class DeviceStatitcisRepository(private val api: Api) : DeviceStatitcisDatasource {
    override fun getDeviceDetail(sn: String): Observable<GetDeviceDetailsssModel> {
        return api.getDeviceDetailssss(SPUtils.getInstance().getString("token"), sn)
    }

    override fun getDeviceStatistics(page: Int, pageSize: Int, statisticTime: String, userid: String, teamnid: String, devicesn: String): Observable<GetDeviceStatisticsModel> {
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
//        if (teamnid == "") {
//            return api.getDeviceStatisticsNoteam(SPUtils.getInstance().getString("token"), page, pageSize, statisticTime, userid, devicesn)
//
//        } else {
//            return api.getDeviceStatistics(SPUtils.getInstance().getString("token"), page, pageSize, statisticTime, userid, teamnid, devicesn)
//
//        }

    }

}