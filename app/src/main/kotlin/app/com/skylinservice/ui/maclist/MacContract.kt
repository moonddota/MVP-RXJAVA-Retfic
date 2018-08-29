package app.com.skylinservice.ui.maclist

import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface MacContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: VlideLogin?) {}
        fun showMaclist(posts: GetMacListModel?) {}
        fun showAPIExcpetion(exception: ApiException)

    }

    interface Presenter : BasePresenter<View> {
        fun getmaclist(tid:String)

    }
}