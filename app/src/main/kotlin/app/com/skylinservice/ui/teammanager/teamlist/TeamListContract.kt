package app.com.skylinservice.ui.teammanager.teamlist

import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface TeamListContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)

        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: Imaketeaminfo) {}
        fun showUserDeleted(posts: VlideNumModel, uid: Int, tid: Long) {}
        fun showAPIExcpetion(exception: ApiException)
        fun showImakeMembers(posts: TeamMemeberModel, tid: Long) {}


    }

    interface Presenter : BasePresenter<View> {
        fun deleteUser(uid: Int, tid: Long)
        fun getIMakeMemers(tid: Long)

    }
}