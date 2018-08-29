package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamIJoinMemeberModel
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface TeamMangerUpdateDataSource {
    fun updateUser(uid: Int, tid: Long, name: String): Observable<VlideNumModel>
}

class TeamMangerUpdateRepository(private val api: Api) : TeamMangerUpdateDataSource {
    override fun updateUser(uid: Int, tid: Long, name: String): Observable<VlideNumModel> {
        return api.upDateTeamMember(SPUtils.getInstance().getString("token"), tid, uid, name)
    }


}