package app.com.skylinservice.ui.personcenter

import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.data.remote.requestmodel.basedata.MemberInfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface DeviceStatisticsContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: GetDeviceDetailsssModel) {}


        fun showStatistics(posts: GetDeviceStatisticsModel) {}


        fun showAPIExcpetion(exception: ApiException)



    }

    interface Presenter : BasePresenter<View> {
        fun getBaseDataStatistics(page: Int, pageSize: Int, statisticTime: String, userid: String, teamnid: String, devicesn: String)

        fun getDeviceDetail(sn : String)
    }
}