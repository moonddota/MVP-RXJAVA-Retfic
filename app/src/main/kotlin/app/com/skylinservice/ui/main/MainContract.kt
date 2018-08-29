package app.com.skylinservice.ui.main

import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface MainContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showAPIExcpetion(exception: ApiException)
        fun showDevicesNum(posts: RequestBIndDevices) {}
        fun showApps(posts: GetAPPsModel) {}
        fun showAuth(posts: RequestBIndDevices) {}
        fun showBanner(posts: GetBannerModel) {}

        fun showLatesInfo(posts: GetAPPInfoModel) {}
        fun showIJoinBack(posts: Imaketeaminfo) {}


        fun showSetDefaultTeam(posts: VlideNumModel) {}




    }

    interface Presenter : BasePresenter<View> {
        fun getBindDevices(userid: Int, page: Int)
        fun getUnUpdateAPPs()
        fun getAuhtes(userid: Int, page: Int)
        fun getBanners(bannername:String)
        fun checkUpdate(id: Int)
        fun getIJoin()



        fun takeDufatltTeam(tid:String)



    }
}