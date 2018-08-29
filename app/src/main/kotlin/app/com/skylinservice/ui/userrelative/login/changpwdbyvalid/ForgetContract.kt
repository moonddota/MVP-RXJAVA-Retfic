package app.com.skylinservice.ui.userrelative.login.changpwdbyvalid

import app.com.skylinservice.data.remote.requestmodel.VlideForget
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface ForgetContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showSendValid(posts: VlideNumModel) {}
        fun showForget(posts: VlideForget) {}
        fun showAPIExcpetion(exception: ApiException)

    }

    interface Presenter : BasePresenter<View> {
        fun sendValidNum(telPhone: String)
        fun forget(telPhone: String, code: String, newpwd: String)
    }
}