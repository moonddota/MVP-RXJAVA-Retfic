package app.com.skylinservice.ui.device

import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface DeviceManagerContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)

        fun showAPIExcpetion(exception: ApiException)

        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: RequestBIndDevices?) {}
    }

    interface Presenter : BasePresenter<View> {
        fun getDevices(uid: String, page: Int)
    }
}