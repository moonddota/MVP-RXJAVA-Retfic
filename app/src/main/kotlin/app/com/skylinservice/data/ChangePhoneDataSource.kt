package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface ChangePhoneDataSource {
    fun sendValidNum(telPhone: String): Observable<VlideNumModel>
    fun sendOrigialValidNum(): Observable<VlideNumModel>
    fun changePhone(orgialCode: String, code: String, newPhone: String): Observable<VlideNumModel>
}

class ChangePhoneRepository(private val api: Api) : ChangePhoneDataSource {
    override fun sendOrigialValidNum(): Observable<VlideNumModel> {
        return api.sendValideNumForYourSelf(SPUtils.getInstance().getString("token"))
    }

    override fun sendValidNum(telPhone: String): Observable<VlideNumModel> {
        return api.sendValideNumForPhone(SPUtils.getInstance().getString("token"), telPhone)
    }

    override fun changePhone(orgialCode: String, code: String, newPhone: String): Observable<VlideNumModel> {
        return api.changeCellPhone(SPUtils.getInstance().getString("token"), newPhone, orgialCode, code)
    }


}