package app.com.skylinservice.ui.device.manual

import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface BindContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: VlideNumModel) {}
        fun showAPIExcpetion(exception: ApiException)

    }

    interface Presenter : BasePresenter<View> {
        fun bind(activenum: String)
    }
}