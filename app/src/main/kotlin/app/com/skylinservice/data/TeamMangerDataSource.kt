package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamIJoinMemeberModel
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface TeamManagerDataSource {
    fun getIMake(): Observable<Imaketeaminfo>
    fun getIJoin(): Observable<Imaketeaminfo>
    fun getImakeMemebers(tid: Long): Observable<TeamMemeberModel>
    fun getIJoinMemebers(tid: Long): Observable<TeamIJoinMemeberModel>
    fun deleteUser(uid: Int, tid: Long): Observable<VlideNumModel>
}

class TeamMangerRepository(private val api: Api) : TeamManagerDataSource {
    override fun deleteUser(uid: Int, tid: Long): Observable<VlideNumModel> {
        return api.deleteTeamMember(SPUtils.getInstance().getString("token"), tid, uid)
    }

    override fun getIJoinMemebers(tid: Long): Observable<TeamIJoinMemeberModel> {
        return api.getIJoinTeamMembers(SPUtils.getInstance().getString("token"), tid)
    }

    override fun getIJoin(): Observable<Imaketeaminfo> {
        return api.ijoinTeamInfo(SPUtils.getInstance().getString("token"))
    }


    override fun getImakeMemebers(tid: Long): Observable<TeamMemeberModel> = api.getTeamMembers(SPUtils.getInstance().getString("token"), tid)

    override fun getIMake(): Observable<Imaketeaminfo> = api.imakeTeamInfo(SPUtils.getInstance().getString("token"))


}