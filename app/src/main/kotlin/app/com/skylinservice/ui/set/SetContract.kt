package app.com.skylinservice.ui.set

import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface SetContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: VlideLogin?) {}
        fun showLatesInfo(posts: GetAPPInfoModel) {}
        fun showlogout() {}
        fun showAPIExcpetion(exception: ApiException)

    }

    interface Presenter : BasePresenter<View> {
        fun logout()
        fun checkUpdate(id: Int)

    }
}