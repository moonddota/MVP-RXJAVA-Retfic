package app.com.skylinservice.ui.teammanager.invite

import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface TeamChangeContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: Imaketeaminfo) {}

        fun showChange(post: VlideNumModel) {}
        fun showAPIExcpetion(exception: ApiException)


    }

    interface Presenter : BasePresenter<View> {
        fun change(tid: Long, uid: Int, name: String)
    }
}