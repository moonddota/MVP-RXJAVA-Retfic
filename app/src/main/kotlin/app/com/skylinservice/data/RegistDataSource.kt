package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.*
import io.reactivex.Observable

interface RegistDataSource {
    fun sendValidNum(telPhone: String): Observable<VlideNumModel>
    fun regist(pwd: String,telphone:String,code:String): Observable<VlideRegist>
    fun weixin_connect(code: String): Observable<WeixinConnectModel>

    fun weixin_bind(wechatId: String, wechatCode: String, telphone: String, telphoneCode: String): Observable<WeixinBindModel>
}

class RegistRepository(private val api: Api) : RegistDataSource {
    override fun regist(pwd: String,telphone:String,code:String): Observable<VlideRegist> {
        return api.register(telphone,pwd,code)
    }

    override fun sendValidNum(telPhone: String): Observable<VlideNumModel> {
        return api.sendValideNumForPhone("",telPhone)
    }

    override fun weixin_bind(wechatId: String, wechatCode: String, telphone: String, telphoneCode: String): Observable<WeixinBindModel> {
        return api.weixin_bind(wechatId, wechatCode, telphone, telphoneCode)
    }

    override fun weixin_connect(code: String): Observable<WeixinConnectModel> {
        return api.weixin_connect(code, "1")
    }


}