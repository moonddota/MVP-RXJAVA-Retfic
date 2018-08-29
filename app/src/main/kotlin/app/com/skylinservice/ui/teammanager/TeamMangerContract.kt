package app.com.skylinservice.ui.teammanager

import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.data.remote.requestmodel.basedata.MemberInfo
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface TeamMangerContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: Imaketeaminfo) {}
        fun showIJoinBack(posts: Imaketeaminfo) {}
        fun showImakeMembers(posts: TeamMemeberModel, tid: Long) {}
        fun showIJoinMembers(posts: TeamMemeberModel, tid: Long) {}

        fun showUserDeleted(uid: Int, tid: Long) {}

        fun showAPIExcpetion(exception: ApiException)



    }

    interface Presenter : BasePresenter<View> {
        fun getIMake()
        fun getIJoin()
        fun getIMakeMemers(tid: Long)
        fun getIJoinMemers(tid: Long)
        fun deleteUser(uid: Int, tid: Long)
    }
}