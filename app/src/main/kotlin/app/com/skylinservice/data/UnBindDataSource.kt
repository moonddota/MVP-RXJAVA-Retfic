package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface UnBindDataSource {
    fun unbind(deviceId: String, deviceSN: String, activeNum: String): Observable<VlideNumModel>
    fun sendValidNum(deviceId: String, telPhone: String): Observable<VlideNumModel>
}

class UnBindRepository(private val api: Api) : UnBindDataSource {
    override fun sendValidNum(deviceId: String, telPhone: String): Observable<VlideNumModel> {
        return api.sendValideNumForPhoneUnbind(SPUtils.getInstance().getString("token"), deviceId, telPhone)
    }

    override fun unbind(deviceId: String, telPhone: String, sms_code: String): Observable<VlideNumModel> {
        return api.unBindDevice(SPUtils.getInstance().getString("token"), deviceId, telPhone, sms_code)
    }
}