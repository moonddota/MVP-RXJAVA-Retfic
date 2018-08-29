package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface TeamEditDataSource {
    fun deleteTeam(tid: Long): Observable<VlideNumModel>
    fun updateTeam(tid: Long, name: String, keytime: Int): Observable<VlideNumModel>
}

class TeamEditRepository(private val api: Api) : TeamEditDataSource {
    override fun updateTeam(tid: Long, name: String, keytime: Int): Observable<VlideNumModel> {
        return api.updateTeam(SPUtils.getInstance().getString("token"), tid.toString(), name, keytime)
    }

    override fun deleteTeam(tid: Long): Observable<VlideNumModel> {
        return api.deleteTeam(SPUtils.getInstance().getString("token"), tid)
    }


}