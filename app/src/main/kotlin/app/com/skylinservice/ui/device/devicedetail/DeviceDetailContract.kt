package app.com.skylinservice.ui.device.devicedetail

import app.com.skylinservice.data.remote.requestmodel.GeneralModel
import app.com.skylinservice.data.remote.requestmodel.GeneralModelData
import app.com.skylinservice.data.remote.requestmodel.GetControlIdModel
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface DeviceDetailContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)

        fun showAPIExcpetion(exception: ApiException)

        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: GetControlIdModel?) {}
    }

    interface Presenter : BasePresenter<View> {
        fun getDeviceDetial(_id: String)
    }
}