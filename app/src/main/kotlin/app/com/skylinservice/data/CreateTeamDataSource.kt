package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface CreateTeamDataSource {
    fun createTeam(name: String, keytime: Int): Observable<VlideLogin>
}

class CreateTeamRepository(private val api: Api) : CreateTeamDataSource {
    override fun createTeam(name: String, keytime: Int): Observable<VlideLogin> {
        return api.ceateTeam(SPUtils.getInstance().getString("token"), name, keytime)
    }

}