package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamIJoinMemeberModel
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface TeamListDataSource {
    fun deleteUser(uid: Int, tid: Long): Observable<VlideNumModel>

    fun getImakeMemebers(tid: Long): Observable<TeamMemeberModel>

}

class TeamListRepository(private val api: Api) : TeamListDataSource {
    override fun deleteUser(uid: Int, tid: Long): Observable<VlideNumModel> {
        return api.deleteTeamMember(SPUtils.getInstance().getString("token"), tid, uid)
    }

    override fun getImakeMemebers(tid: Long): Observable<TeamMemeberModel> = api.getTeamMembers(SPUtils.getInstance().getString("token"), tid)


}