package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.*
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface MainDataSource {
    fun getDevices(userid: Int, page: Int): Observable<RequestBIndDevices>

    fun getBanner(bannername: String): Observable<GetBannerModel>

    fun getUnUpdateApps(): Observable<GetAPPsModel>

    fun getDetials(id: Int): Observable<GetAPPInfoModel>


    fun getAuthes(userid: Int, page: Int): Observable<RequestBIndDevices>

    fun getLatesVersionInfo(id: Int): Observable<GetAPPInfoModel>

    fun getIJoin(): Observable<Imaketeaminfo>

    fun getSetDefualtTeam(team_id:String):Observable<VlideNumModel>


}

class MainRepository(private val api: Api) : MainDataSource {
    override fun getSetDefualtTeam(team_id:String): Observable<VlideNumModel> {
        return  api.setDefaultTieam(SPUtils.getInstance().getString("token"), team_id)
    }

    override fun getDetials(id: Int): Observable<GetAPPInfoModel> {
        return api.newestAppInfo(id)
    }

    override fun getUnUpdateApps(): Observable<GetAPPsModel> {
        return api.getNativeApp()
    }

    override fun getAuthes(userid: Int, page: Int): Observable<RequestBIndDevices> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBanner(bannername: String): Observable<GetBannerModel> {
        return api.getBannerConfig(SPUtils.getInstance().getString("token"), bannername)

    }

    override fun getDevices(userid: Int, page: Int): Observable<RequestBIndDevices> {
        var range = "{\"device_info.create_time\":-1}"
        return api.getBindDevices(SPUtils.getInstance().getString("token"), userid.toString(), 1, range)
    }
    override fun getLatesVersionInfo(id: Int): Observable<GetAPPInfoModel> {
        return api.newestAppInfo(id)
    }

    override fun getIJoin(): Observable<Imaketeaminfo> {
        return api.ijoinTeamInfo(SPUtils.getInstance().getString("token"))
    }


}