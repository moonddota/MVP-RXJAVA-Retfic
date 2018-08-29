package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.GeneralModel
import app.com.skylinservice.data.remote.requestmodel.GeneralModelData
import app.com.skylinservice.data.remote.requestmodel.GetControlIdModel
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface DeviceDetailDataSource {
    fun getDeviceDetail(_id: String): Observable<GetControlIdModel>


}

class DeviceDetailRepository(private val api: Api) : DeviceDetailDataSource {

    override fun getDeviceDetail(_id: String): Observable<GetControlIdModel> {
        return api.getDeviceDetail(SPUtils.getInstance().getString("token"), _id)
    }

}