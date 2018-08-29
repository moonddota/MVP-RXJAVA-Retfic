package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import io.reactivex.Observable

interface SetDataSource {
    fun getLatesVersionInfo(id: Int): Observable<GetAPPInfoModel>

}

class SetRepository(private val api: Api) : SetDataSource {
    override fun getLatesVersionInfo(id: Int): Observable<GetAPPInfoModel> {
        return api.newestAppInfo(id)
    }
}