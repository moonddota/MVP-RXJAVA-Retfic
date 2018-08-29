package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.*
import io.reactivex.Observable

interface LoginDataSource {
    fun sendValidNum(telPhone: String): Observable<VlideNumModel>

    fun logining(telphone: String, pwd: String): Observable<VlideLogin>


    fun weixin_connect(code: String): Observable<WeixinConnectModel>


    fun getLatesVersionInfo(id: Int): Observable<GetAPPInfoModel>


    fun weixin_bind(wechatId: String, wechatCode: String, telphone: String, telphoneCode: String): Observable<WeixinBindModel>
}

class LoginRepository(private val api: Api) : LoginDataSource {
    override fun weixin_bind(wechatId: String, wechatCode: String, telphone: String, telphoneCode: String): Observable<WeixinBindModel> {
        return api.weixin_bind(wechatId, wechatCode, telphone, telphoneCode)
    }

    override fun weixin_connect(code: String): Observable<WeixinConnectModel> {
        return api.weixin_connect(code, "1")
    }

    override fun logining(telphone: String, pwd: String): Observable<VlideLogin> {
        return api.logina(telphone, pwd)
    }

    override fun sendValidNum(telPhone: String): Observable<VlideNumModel> {
        return api.sendValideNumForPhone("", telPhone)
    }

    override fun getLatesVersionInfo(id: Int): Observable<GetAPPInfoModel> {
        return api.newestAppInfo(id)
    }


}