package app.com.skylinservice.ui.personcenter

import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import app.com.skylinservice.data.remote.requestmodel.basedata.MemberInfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface DetailStatisticsContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()

        fun showRequestAnswer(posts: GetDeviceDetailsssModel) {}


        fun showStatistics(posts: GetDeviceStatisticsModel) {}
        fun showAPIExcpetion(exception: ApiException)

        fun showIJoinBack(posts: Imaketeaminfo) {}

        fun showMaclist(posts: GetMacListModel?) {}

        fun showAllOperatier(posts: GetALlOperaterModel?) {}


    }

    interface Presenter : BasePresenter<View> {
        fun getBaseDataStatistics(page: Int, pageSize: Int, statisticTime: String, userid: String, teamnid: String, devicesn: String)

        fun getDeviceDetail(sn: String)

        fun getmaclist()

        fun getIJoin()

        fun getAllOperater(device_sn: String)
    }
}