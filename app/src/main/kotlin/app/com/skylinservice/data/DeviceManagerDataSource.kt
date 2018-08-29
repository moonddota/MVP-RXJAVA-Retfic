package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.GetControlIdModel
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface DeviceManagerDataSource {
    fun getDevices(userid: Int, page: Int): Observable<RequestBIndDevices>

    fun getControlId(id: String): Observable<GetControlIdModel>

}

class DeviceManagerRepository(private val api: Api) : DeviceManagerDataSource {
    override fun getControlId(id: String): Observable<GetControlIdModel> {
        return api.getControlId(SPUtils.getInstance().getString("token"), id)
    }

    override fun getDevices(userid: Int, page: Int): Observable<RequestBIndDevices> {
        var range = "{\"device_info.create_time\":-1}"
        return api.getBindDevices(SPUtils.getInstance().getString("token"), userid.toString(), 1, range)
    }

}