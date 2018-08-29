package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.VlideForget
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface ForgetDataSource {
    fun sendValidNum(telPhone: String): Observable<VlideNumModel>
    fun fprget(telPhone: String, code: String, newpwd: String): Observable<VlideForget>
}

class ForgetRepository(private val api: Api) : ForgetDataSource {
    override fun sendValidNum(telPhone: String): Observable<VlideNumModel> {
        return api.sendValideNumForPhone(SPUtils.getInstance().getString("token"), telPhone)
    }

    override fun fprget(telPhone: String, code: String, newpwd: String): Observable<VlideForget> {
        return api.forGotPWD(SPUtils.getInstance().getString("token"), telPhone, code, newpwd)
    }


}