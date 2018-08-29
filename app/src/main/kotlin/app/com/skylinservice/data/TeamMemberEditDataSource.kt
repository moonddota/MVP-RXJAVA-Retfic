package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface TeamMemberDataDataSource {
    fun updateTeamMember(tid: Int, name: String, keytime: Int): Observable<VlideNumModel>
}

class TeamMemberRepository(private val api: Api) : TeamMemberDataDataSource {
    override fun updateTeamMember(tid: Int, name: String, keytime: Int): Observable<VlideNumModel> {
        return null!!
    }


}