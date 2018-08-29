package app.com.skylinservice.ui.softcenter

import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView
import io.reactivex.Observable

interface SoftCenterContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showgetSoftApp(posts: GetAPPsModel) {}
        fun showgetSoftAppDetail(posts: GetAPPInfoModel) {}
        fun showgetFinalApps(posts: List<NativeAPP>) {}
        fun showAPIExcpetion(exception: ApiException)


    }

    interface Presenter : BasePresenter<View> {
        fun getSoftApp()
        fun getSoftAppDetail(id: Int)
        fun etFinalApps()


    }
}