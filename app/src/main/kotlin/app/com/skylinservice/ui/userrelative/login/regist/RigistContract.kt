package app.com.skylinservice.ui.userrelative.login.regist

import app.com.skylinservice.data.remote.model.Post
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface RigistContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)

        fun showRigst(posts: VlideRegist)
        fun showSend(posts: VlideNumModel)

        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: VlideLogin) {}
        fun showAPIExcpetion(exception: ApiException)


        fun showweixinBind(post: WeixinBindModel)

        fun showweixinConnect(post: WeixinConnectModel)

    }

    interface Presenter : BasePresenter<View> {
        fun sendValidNum(telPhone: String)
        fun register(pwd: String,telphone:String,code:String)
        fun weixin_bind(wechatId: String, wechatCode: String, telphone: String, telphoneCode: String)
        fun weixin_connect(code: String)


    }
}