package app.com.skylinservice.ui.userrelative.login

import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface LoginContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showAPIExcpetion(exception: ApiException)

        fun showUnknownError()
        fun showRequestAnswer(posts: VlideLogin) {}
        fun showSend(posts: VlideNumModel)

        fun showweixinConnect(post: WeixinConnectModel)

        fun showweixinBind(post: WeixinBindModel)


        fun showLatesInfo(posts: GetAPPInfoModel) {}
    }

    interface Presenter : BasePresenter<View> {

        fun checkUpdate(id: Int)


        fun login(telPhone: String, pwd: String)
        fun sendValidNum(telPhone: String)

        fun weixin_connect(code: String)

        fun weixin_bind(wechatId: String, wechatCode: String, telphone: String, telphoneCode: String)

    }
}