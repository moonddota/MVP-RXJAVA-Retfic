package app.com.skylinservice.ui.personcenter

import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.data.remote.requestmodel.basedata.MemberInfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface PersionStatisticsContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: Imaketeaminfo) {}


        fun showAPIExcpetion(exception: ApiException)



    }

    interface Presenter : BasePresenter<View> {
        fun getBaseDataStatistics(uid: Int, tid: Long)
    }
}