package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface BindDataSource {
    fun bind(activeNum: String): Observable<VlideNumModel>
}

class BindRepository(private val api: Api) : BindDataSource {
    override fun bind(activeNum: String): Observable<VlideNumModel> {
        return api.bindDevice(SPUtils.getInstance().getString("token"), activeNum)
    }
}