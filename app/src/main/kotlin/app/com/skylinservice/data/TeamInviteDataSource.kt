package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface TeamInviteDataSource {
    fun inviteteam(tid: Long, name: String, telphone: String): Observable<VlideNumModel>
}

class TeamInviteRepository(private val api: Api) : TeamInviteDataSource {
    override fun inviteteam(tid: Long, name: String, telphone: String): Observable<VlideNumModel> {
        return api.addTeamMember(SPUtils.getInstance().getString("token"), tid, name, telphone)
    }
}