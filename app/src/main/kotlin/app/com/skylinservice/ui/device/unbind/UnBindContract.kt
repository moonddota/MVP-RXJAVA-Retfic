package app.com.skylinservice.ui.device.unbind

import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface UnBindContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)

        fun showAPIExcpetion(exception: ApiException)

        fun showNoConnectivityError()
        fun showUnknownError()
        fun showunbind(posts: VlideNumModel) {}
        fun showsendValidNum(posts: VlideNumModel) {}

    }

    interface Presenter : BasePresenter<View> {
        fun unbind(deviceId: String, telPhone: String, sms_code: String)
        fun sendValidNum(deviceId: String, telPhone: String)
    }
}