package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface ChangNickNameDataSource {
    fun upuser(name: String): Observable<VlideNumModel>
    fun upFly(_id: String,name: String): Observable<VlideNumModel>
}

class ChangNickNameRepository(private val api: Api) : ChangNickNameDataSource {
    override fun upFly(_id: String,name: String): Observable<VlideNumModel> {
        return api.upDateDeviceInfo(SPUtils.getInstance().getString("token"),_id, name)
    }

    override fun upuser(name: String): Observable<VlideNumModel> {
        return api.upUser(SPUtils.getInstance().getString("token"), name)
    }

}